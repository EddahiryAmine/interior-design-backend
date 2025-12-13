# utils/media_client.py
import requests

def upload_png_to_media(media_upload_url: str, png_bytes: bytes) -> dict:
    files = {"file": ("generated.png", png_bytes, "image/png")}
    r = requests.post(media_upload_url, files=files, timeout=120)
    r.raise_for_status()
    return r.json()
