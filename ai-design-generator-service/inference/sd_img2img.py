import torch
from PIL import Image
from diffusers import StableDiffusionImg2ImgPipeline
import numpy as np
import warnings

warnings.filterwarnings("ignore")

torch.set_default_device('cpu')
torch.set_default_dtype(torch.float32)

_PIPE = None

def get_pipe(model_id: str = "runwayml/stable-diffusion-v1-5"):
    global _PIPE
    
    if _PIPE is not None:
        return _PIPE
    
    print(f" Loading pipeline on CPU for model: {model_id}")
    
    device = torch.device("cpu")
    print(f" Using device: {device}")
    
    pipe = StableDiffusionImg2ImgPipeline.from_pretrained(
        model_id,
        torch_dtype=torch.float32,
        safety_checker=None,
        requires_safety_checker=False,
    )
    
    pipe = pipe.to(device)
    
    pipe.enable_attention_slicing()
  
    pipe.eval()
    
    _PIPE = (pipe, device)
    return _PIPE


def run_img2img(
    init_image: Image.Image,
    prompt: str,
    strength: float = 0.45,
    guidance: float = 6.5,
    steps: int = 15,
    model_id: str = "runwayml/stable-diffusion-v1-5",
):
    pipe_tuple = get_pipe(model_id)
    pipe, device = pipe_tuple
    
    init_image = init_image.convert("RGB").resize((512, 512))
    
 
    from diffusers.image_processor import VaeImageProcessor
    
    processor = VaeImageProcessor()
    
    image_tensor = processor.preprocess(
        init_image,
        height=512,
        width=512
    )
    
    image_tensor = image_tensor.to(device=device, dtype=torch.float32)
    
    do_cfg = guidance > 1.0
    
    with torch.no_grad():
        prompt_embeds, negative_prompt_embeds = pipe.encode_prompt(
            prompt=prompt,
            device=device,
            num_images_per_prompt=1,
            do_classifier_free_guidance=do_cfg,
            negative_prompt="",
        )
    
    generator = torch.Generator(device=device)
    
    with torch.no_grad():
        result = pipe(
            prompt_embeds=prompt_embeds,
            negative_prompt_embeds=negative_prompt_embeds,
            image=image_tensor,
            strength=float(strength),
            guidance_scale=float(guidance),
            num_inference_steps=int(steps),
            generator=generator,
            output_type="pil",
        )
    
    return result.images[0]