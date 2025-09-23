# 💪 YourRoom – Backend con Spring Boot

Este es el backend de **YourRoom**, una aplicación pensada para entrenadores personales que desean reservar salas por horas en gimnasios u hoteles.  
El backend está desarrollado con **Spring Boot**, usa **JWT para autenticación** y se conecta a **Firebase Storage** para el almacenamiento de imágenes de perfil.  
Los datos de usuario y autenticación se guardan en una base de datos **MySQL**.

---

## 📦 Tecnologías utilizadas

- 🧠 Java 17
- ⚙️ Spring Boot
- 🔐 JWT (JSON Web Token)
- 🗂️ Firebase Storage
- 🐬 MySQL
- 🧰 Maven

---

## 🚀 Funcionalidades principales

- Registro e inicio de sesión de usuarios con JWT
- Validación de credenciales desde el backend
- Gestión y edición de perfil de usuario
- Subida de imágenes de perfil a Firebase Storage
- API REST conectada a base de datos MySQL

---

## ✅ Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener:

- Java 17 o superior
- Maven instalado
- MySQL funcionando en tu máquina (por defecto usa `localhost:3307`)
- Archivo de credenciales Firebase (`your-room-4277c-firebase-adminsdk-xxxx.json`) proporcionado por el autor

---

## ⚙️ Instalación y ejecución en local

1. **Clona este repositorio:**

```bash
git clone https://github.com/tu-usuario/yourroom-backend.git
cd yourroom-backend
```

2. **Crea una carpeta llamada `secrets/` en la raíz del proyecto** y añade dentro el archivo `.json` con las credenciales de Firebase Storage (que te será proporcionado por el autor del proyecto).

3. **Define la variable de entorno con la ruta a las credenciales:**

En Linux/macOS:
```bash
export GOOGLE_APPLICATION_CREDENTIALS=./secrets/your-room-4277c-firebase-adminsdk-xxxx.json
```

En Windows (CMD):
```cmd
set GOOGLE_APPLICATION_CREDENTIALS=.\secrets\your-room-4277c-firebase-adminsdk-xxxx.json
```

4. **Verifica la configuración de MySQL** en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/yourroom_db
spring.datasource.username=root
spring.datasource.password=123456
```

⚠️ Cambia los datos según tu configuración local de MySQL.

5. **Ejecuta la aplicación:**

```bash
./mvnw spring-boot:run
```

---

## 🔗 Endpoints disponibles (ejemplo)

| Método | Ruta                  | Descripción                      |
|--------|-----------------------|----------------------------------|
| POST   | /api/auth/register    | Registro de usuarios (JWT)       |
| POST   | /api/auth/login       | Inicio de sesión (JWT)           |
| GET    | /api/user/profile     | Obtener perfil de usuario        |
| PUT    | /api/user/profile     | Actualizar datos del perfil      |
| POST   | /api/user/profile/img | Subida de imagen de perfil (Firebase Storage) |

---

## 🔐 Seguridad del proyecto

- El archivo de credenciales de Firebase Storage **NO está incluido** en este repositorio.
- Está ignorado mediante `.gitignore` y debe colocarse manualmente en `./secrets/`.
- Si necesitas el archivo para pruebas, solicítalo al autor del proyecto.

---

## 👨‍🏫 Para profesores o revisores

Este proyecto está preparado para ser ejecutado en local con facilidad.  
Solo necesitas:
- El archivo de credenciales proporcionado (para pruebas con Firebase Storage)
- Tener Java, Maven y MySQL instalados
- Definir la variable de entorno con la clave

---

## 📄 Licencia

Este proyecto ha sido desarrollado como parte del ciclo **Desarrollo de Aplicaciones Multiplataforma** y se entrega como práctica personal.  
Uso académico. Todos los derechos reservados por el autor.

---

## 👤 Autor

**Javier Serrano**  
Desarrollador de apps multiplataforma  
[GitHub](https://github.com/tu-usuario) | [LinkedIn](https://linkedin.com/in/tu-perfil)