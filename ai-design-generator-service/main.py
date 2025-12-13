# main.py - VERSION SIMPLIFIÉE POUR CPU
import os
import sys

# ==================== FORÇAGE CPU ====================
# AVANT TOUT IMPORT
os.environ["CUDA_VISIBLE_DEVICES"] = "-1"
os.environ["PYTORCH_ENABLE_MPS_FALLBACK"] = "0"

print("🔧 Initialisation en mode CPU...")

# Import PyTorch
import torch
torch.set_default_device('cpu')
torch.set_default_dtype(torch.float32)

print(f"✅ PyTorch configuré: {torch.device('cpu')}")

# ==================== IMPORTS ====================
from fastapi import FastAPI
from pydantic import BaseModel
from io import BytesIO
from PIL import Image
import requests

print("📦 Modules chargés")

# ==================== FONCTIONS ====================
def download_image(image_url: str) -> Image.Image:
    """Télécharge une image depuis une URL"""
    resp = requests.get(image_url, timeout=30)
    resp.raise_for_status()
    return Image.open(BytesIO(resp.content)).convert("RGB")

def upload_png_to_media(media_upload_url: str, png_bytes: bytes) -> dict:
    """Upload une image PNG vers le service media"""
    files = {"file": ("generated.png", png_bytes, "image/png")}
    r = requests.post(media_upload_url, files=files, timeout=60)
    r.raise_for_status()
    return r.json()

# ==================== MODÈLE STABLE DIFFUSION ====================
_PIPE = None

def get_pipeline():
    """Charge le modèle Stable Diffusion une seule fois"""
    global _PIPE
    
    if _PIPE is not None:
        return _PIPE
    
    print("⚙️ Chargement du modèle Stable Diffusion...")
    
    from diffusers import StableDiffusionImg2ImgPipeline
    
    # Charger le modèle
    pipe = StableDiffusionImg2ImgPipeline.from_pretrained(
        "runwayml/stable-diffusion-v1-5",
        torch_dtype=torch.float32,
        safety_checker=None,
        requires_safety_checker=False,
    )
    
    # Forcer CPU
    pipe = pipe.to('cpu')
    
    # Optimisations
    pipe.enable_attention_slicing()
    
    print("✅ Modèle chargé!")
    _PIPE = pipe
    return _PIPE

def run_img2img(init_image, prompt, strength=0.55, guidance=7.0, steps=20):
    """Génère une image avec img2img"""
    pipe = get_pipeline()
    
    # Préparer l'image
    init_image = init_image.convert("RGB").resize((512, 512))
    
    # Générer
    result = pipe(
        prompt=prompt,
        image=init_image,
        strength=strength,
        guidance_scale=guidance,
        num_inference_steps=steps,
        output_type="pil",
    )
    
    return result.images[0]

# ==================== APPLICATION FASTAPI ====================
app = FastAPI()

MEDIA_UPLOAD_URL = "http://localhost:8082/media/upload"

class GenerationRequest(BaseModel):
    imageUrl: str
    prompt: str
    strength: float = 0.55
    guidance: float = 7.0
    steps: int = 20

@app.get("/")
def root():
    return {"message": "AI Design Generator Service (CPU Mode)"}

@app.get("/health")
def health():
    return {
        "status": "healthy",
        "device": "cpu",
        "pytorch": torch.__version__,
        "directml_installed": False
    }

@app.post("/generate")
def generate(req: GenerationRequest):
    print(f"📥 Requête: {req.prompt[:50]}...")
    
    # Télécharger
    init_img = download_image(req.imageUrl)
    print(f"✅ Image: {init_img.size}")
    
    # Générer
    out_img = run_img2img(
        init_image=init_img,
        prompt=req.prompt,
        strength=req.strength,
        guidance=req.guidance,
        steps=req.steps,
    )
    
    print(f"✅ Généré: {out_img.size}")
    
    # Convertir
    buf = BytesIO()
    out_img.save(buf, format="PNG")
    png_bytes = buf.getvalue()
    
    # Upload
    res = upload_png_to_media(MEDIA_UPLOAD_URL, png_bytes)
    
    return {
        "ok": True,
        "mode": "cpu",
        "generatedImageUrl": res["url"],
        "filename": res["filename"]
    }

print("\n" + "=" * 50)
print(" Serveur prêt! Accédez à: http://localhost:8001")
print("=" * 50)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8001)