### 📱 PharmaStockGSB
🏥 Application de gestion de stock de médicaments

### 📌 Présentation
PharmaStockGSB est une application mobile Android permettant de gérer les stocks de médicaments du laboratoire GSB.
Elle offre une interface intuitive pour suivre les médicaments, recevoir des alertes sur les stocks faibles et les dates de péremption, et administrer les utilisateurs et catégories.

### 🚀 Fonctionnalités principales
✔️ Gestion des médicaments (CRUD)

✔️ Catégorisation des médicaments

✔️ Alertes sur les stocks faibles et dates de péremption

✔️ Gestion des utilisateurs (Admin)

✔️ Authentification sécurisée avec JWT

✔️ Interface fluide et ergonomique

### ⚙️ Technologies utilisées
Langage : Java

Architecture : MVVM (Model-View-ViewModel)

Communication API : Volley

Sécurité : JWT Token pour l'authentification

Gestion des dépendances : Gradle

### 📥 Installation
🔧 Prérequis

✅ Android Studio installé

✅ SDK Android 26+

✅ Connexion à l'API PharmaStockGSB

✅ Émulateur ou appareil physique Android

### 📲 Étapes d’installation
1️⃣ Cloner le projet :
```bash
git clone https://github.com/Kenji-Or/PharmaStockGSB.git
cd PharmaStockGSB
```
2️⃣ Ouvrir avec Android Studio

3️⃣ Lancer la synchronisation Gradle

4️⃣ Configurer l’URL de l’API dans java>com.example.gsb>network>ApiService.java :
```bash
    public static final String BASE_URL = "http://10.0.2.2:5000/"; // Modifier selon le backend
```
5️⃣ Compiler et exécuter l’application 🎉

### 📂 Arborescence du projet
```textplain
📂 PharmaStockGSB  
 ┣ 📂 app  
 ┃ ┣ 📂 src/main/java/com/gsb/pharmastock  
 ┃ ┃ ┣ 📂 ui                 # Activités et fragments UI  
 ┃ ┃ ┣ 📂 data               # Gestion des sources de données etClasses des modèles
 ┃ ┃ ┃ ┣ 📂 repositories     # Gestion des accès aux données
 ┃ ┃ ┃ ┗ 📂 model            # Modélisation des données
 ┃ ┃ ┣ 📂 network            # API (Volley)
 ┃ ┃ ┃ ┗ 📜 ApiService.java  # Interface des endpoints
 ┃ ┃ ┗ 📂 utils              # Fonctions utilitaires  
 ┃ ┣ 📂 res  
 ┃ ┃ ┣ 📂 layout             # XML des interfaces  
 ┃ ┃ ┣ 📂 drawable           # Icônes et images
 ┃ ┃ ┣ 📂 menu               # menu
 ┃ ┃ ┗ 📂 values             # Fichiers de ressources (strings, styles)  
 ┣ 📄 AndroidManifest.xml     # Configuration de l’application  
 ┣ 📄 build.gradle           # Configuration Gradle  
 ┣ 📄 README.md              # Documentation  
```

### 🔑 Accès à l’application
📥 Télécharger l’APK : [PharmaStockGSB](https://github.com/Kenji-Or/PharmaStockGSB/releases/download/V1.0/app-release.apk)

🔹 Compte utilisateur

✉️ Email : paul.logan@mail.com

🔑 Mot de passe : 12345678

🔹 Compte administrateur

✉️ Email : admin.gsb@mail.com

🔑 Mot de passe : 987654321

### 🖥️API
👉 Lien vers le dépôt de l'API nécessaire au fonctionnement de l'application : [API PharmaStockGSB](https://github.com/Kenji-Or/API_GSB_PharmaStock)

### ✍️Auteur
👨‍💻 Développé par : Kenji Ogier

📅 Dernière mise à jour : 27/03/2025
