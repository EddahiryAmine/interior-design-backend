import os
import sys
import torch

print("=" * 60)
print("VÉRIFICATION DE L'ENVIRONNEMENT PYTHON/PYTORCH")
print("=" * 60)

print("\n📁 Informations système:")
print(f"   Python: {sys.version}")
print(f"   Répertoire courant: {os.getcwd()}")

print("\n🔧 Variables d'environnement importantes:")
env_vars = [
    "CUDA_VISIBLE_DEVICES",
    "PYTORCH_ENABLE_MPS_FALLBACK", 
    "PYTORCH_DIRECTML_DISABLE",
    "PATH"
]

for var in env_vars:
    value = os.environ.get(var, "Non défini")
    if var == "PATH":
        print(f"   {var}: [présent, longueur: {len(value)}]")
    else:
        print(f"   {var}: {value}")

print("\n💻 Configuration PyTorch:")
print(f"   Version PyTorch: {torch.__version__}")
print(f"   Version CUDA: {torch.version.cuda if hasattr(torch.version, 'cuda') else 'N/A'}")
print(f"   CUDA disponible: {torch.cuda.is_available()}")
print(f"   Nombre de GPUs CUDA: {torch.cuda.device_count() if torch.cuda.is_available() else 0}")

print("\n🎯 Devices disponibles:")
print(f"   CPU: {torch.device('cpu')}")

try:
    import torch_directml
    print(f"   DirectML disponible: OUI")
    print(f"   DirectML device: {torch_directml.device()}")
    print(f"   Nombre de devices DirectML: {torch_directml.device_count()}")
except ImportError:
    print(f"   DirectML disponible: NON (non installé)")
except Exception as e:
    print(f"   DirectML erreur: {str(e)[:100]}")

try:
    if hasattr(torch.backends, 'mps') and torch.backends.mps.is_available():
        print(f"   MPS (Mac) disponible: OUI")
    else:
        print(f"   MPS (Mac) disponible: NON")
except:
    print(f"   MPS (Mac): erreur de vérification")

print("\n🧪 Test création de tensors:")
try:
    x_cpu = torch.randn(2, 3, device='cpu')
    print(f"   Tensor CPU créé: {x_cpu.device}, dtype: {x_cpu.dtype}")
    
    y_cpu = torch.randn(3, 2, device='cpu')
    z_cpu = x_cpu @ y_cpu
    print(f"   Multiplication CPU réussie: {z_cpu.shape}")
    
    try:
        import torch_directml
        dml = torch_directml.device()
        x_dml = torch.randn(2, 3, device=dml)
        print(f"   Tensor DirectML créé: {x_dml.device}, dtype: {x_dml.dtype}")
    except:
        pass
        
except Exception as e:
    print(f"   ❌ Erreur lors des tests: {e}")

print("\n📦 Packages installés:")
try:
    import pkg_resources
    packages = ["torch", "torchvision", "torchaudio", "diffusers", "transformers", "torch-directml"]
    for pkg in packages:
        try:
            version = pkg_resources.get_distribution(pkg).version
            print(f"   {pkg}: {version}")
        except:
            print(f"   {pkg}: NON INSTALLÉ")
except:
    print("   Impossible de vérifier les packages")

print("\n" + "=" * 60)
print("ANALYSE:")
print("=" * 60)

issues = []
if torch.cuda.is_available():
    print("⚠️  CUDA est disponible - risque de conflit avec DirectML/CPU")
    issues.append("CUDA activé")

try:
    import torch_directml
    print("⚠️  DirectML est installé - peut causer des conflits")
    issues.append("DirectML installé")
except:
    print("✅ DirectML non installé - bon pour CPU pur")

if os.environ.get("CUDA_VISIBLE_DEVICES") != "-1":
    print("❌ CUDA_VISIBLE_DEVICES n'est pas défini à '-1'")
    issues.append("CUDA_VISIBLE_DEVICES incorrect")
else:
    print("✅ CUDA_VISIBLE_DEVICES = '-1' (CUDA désactivé)")

if issues:
    print(f"\n🚨 PROBLÈMES DÉTECTÉS: {len(issues)}")
    for i, issue in enumerate(issues, 1):
        print(f"   {i}. {issue}")
    print("\n💡 RECOMMANDATIONS:")
    print("   1. Assurez-vous que CUDA_VISIBLE_DEVICES=-1")
    print("   2. Désinstallez torch-directml: pip uninstall torch-directml")
    print("   3. Forcez CPU dans votre code: torch.set_default_device('cpu')")
else:
    print("\n✅ Environnement correct pour CPU!")

print("\n" + "=" * 60)