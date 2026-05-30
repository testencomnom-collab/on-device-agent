import os
from PIL import Image, ImageDraw

input_path = r"C:\Users\Yunes\.gemini\antigravity\brain\60339e60-e55b-4669-a50a-c18ff0ba4065\media__1780177277515.jpg"
res_path = r"app\src\main\res"

sizes = {
    "mdpi": 48,
    "hdpi": 72,
    "xhdpi": 96,
    "xxhdpi": 144,
    "xxxhdpi": 192
}

def create_icons():
    img = Image.open(input_path).convert("RGBA")
    w, h = img.size
    
    # Crop to a square containing the badge
    # The badge is in the upper middle. Let's do a center crop, but shifted slightly if needed.
    # Actually, a center crop is usually best.
    size = min(w, h)
    
    # If the image is vertical (like a phone screenshot), the badge is often vertically centered.
    left = (w - size) // 2
    top = (h - size) // 2
    
    # For this specific image (tall aspect ratio), let's crop around the center where the badge is.
    # Assuming the badge is perfectly centered.
    square_img = img.crop((left, top, left + size, top + size))
    
    for density, dim in sizes.items():
        # Create standard icon
        resized = square_img.resize((dim, dim), Image.Resampling.LANCZOS)
        
        out_dir = os.path.join(res_path, f"mipmap-{density}")
        os.makedirs(out_dir, exist_ok=True)
        
        # Save ic_launcher.png
        resized.save(os.path.join(out_dir, "ic_launcher.png"))
        
        # Create round icon
        mask = Image.new("L", (dim, dim), 0)
        draw = ImageDraw.Draw(mask)
        draw.ellipse((0, 0, dim, dim), fill=255)
        
        round_img = resized.copy()
        round_img.putalpha(mask)
        
        round_img.save(os.path.join(out_dir, "ic_launcher_round.png"))

if __name__ == "__main__":
    create_icons()
    print("Icons generated successfully!")
