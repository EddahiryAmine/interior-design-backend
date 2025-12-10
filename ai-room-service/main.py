# ai-room-service/main.py

import io
import os
from typing import List

import numpy as np
import onnxruntime as ort
import requests
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from PIL import Image
from fastapi import File, UploadFile
# ========= CONFIG =========

MODEL_PATH = os.path.join("model", "rooms_classifier_finetuned.onnx")

CLASS_NAMES = [
    "bathroom",
    "bedroom",
    "kitchen",
    "living",
    "office"
]


IMG_SIZE = 224



app = FastAPI(
    title="AI Room Type Service",
    description="Microservice pour prédire le type de pièce à partir d'une image",
    version="1.0.0",
)


class PredictRequest(BaseModel):
    imageUrl: str


class PredictResponse(BaseModel):
    roomType: str
    confidence: float



print(" Chargement du modèle ONNX...")
if not os.path.exists(MODEL_PATH):
    raise RuntimeError(f"Model file not found at {MODEL_PATH}")

session = ort.InferenceSession(
    MODEL_PATH,
    providers=["CPUExecutionProvider"]
)
input_name = session.get_inputs()[0].name
output_name = session.get_outputs()[0].name
print("✅ Modèle ONNX chargé !")
print("Input name:", input_name)
print("Output name:", output_name)


# ========= PREPROCESS =========

def download_image(url: str) -> Image.Image:
    """Télécharger l'image à partir d'une URL et la charger avec PIL."""
    try:
        resp = requests.get(url, timeout=10)
        resp.raise_for_status()
        img_bytes = io.BytesIO(resp.content)
        img = Image.open(img_bytes).convert("RGB")
        return img
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Erreur de téléchargement de l'image: {e}")


def preprocess_image(img: Image.Image) -> np.ndarray:
    """
    Prétraitement de l'image pour matcher le modèle ONNX :
    - resize à IMG_SIZE x IMG_SIZE
    - conversion en numpy
    - normalisation [0,1]
    - mise en forme (1, 3, H, W)
    ⚠️ Si dans ton notebook tu utilisais une normalisation différente
       (mean/std spécifiques), il faudra l'ajuster ici.
    """
    img = img.resize((IMG_SIZE, IMG_SIZE))
    arr = np.array(img).astype("float32") / 255.0  # [H, W, 3]

    # [H, W, 3] -> [3, H, W]
    arr = np.transpose(arr, (2, 0, 1))

    # batch dimension
    arr = np.expand_dims(arr, axis=0)  # [1, 3, H, W]

    return arr


def softmax(x: np.ndarray) -> np.ndarray:
    e_x = np.exp(x - np.max(x))
    return e_x / e_x.sum(axis=-1, keepdims=True)


# ========= INFERENCE =========

def predict_room_type(image_url: str) -> PredictResponse:
    # 1) Télécharger image
    img = download_image(image_url)

    # 2) Prétraitement
    input_tensor = preprocess_image(img)  # [1, 3, 224, 224]

    # 3) Inference ONNX
    outputs = session.run([output_name], {input_name: input_tensor})[0]  # [1, num_classes]

    # 4) Softmax + argmax
    probs = softmax(outputs)[0]  # [num_classes]
    pred_idx = int(np.argmax(probs))
    confidence = float(probs[pred_idx])

    if pred_idx < 0 or pred_idx >= len(CLASS_NAMES):
        room_type = "unknown"
    else:
        room_type = CLASS_NAMES[pred_idx]

    return PredictResponse(roomType=room_type, confidence=confidence)


# ========= ENDPOINTS =========

@app.get("/")
def root():
    return {"message": "AI Room Type Service is running"}


@app.post("/predict", response_model=PredictResponse)
def predict_endpoint(req: PredictRequest):
    """
    Ex: POST /predict
    Body: { "imageUrl": "http://localhost:8086/media/files/xxx.jpg" }
    """
    if not req.imageUrl:
        raise HTTPException(status_code=400, detail="imageUrl is required")

    result = predict_room_type(req.imageUrl)
    return result
@app.post("/predict-file")
def predict_file(file: UploadFile = File(...)):
    try:
        img_bytes = file.file.read()
        img = Image.open(io.BytesIO(img_bytes)).convert("RGB")

        input_tensor = preprocess_image(img)

        outputs = session.run([output_name], {input_name: input_tensor})[0]
        probs = softmax(outputs)[0]
        pred_idx = int(np.argmax(probs))
        confidence = float(probs[pred_idx])
        room_type = CLASS_NAMES[pred_idx]

        return {
            "roomType": room_type,
            "confidence": confidence
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))