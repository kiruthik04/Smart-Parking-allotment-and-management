# Deployment Guide: Free Tier Stack

This guide will walk you through deploying your Java Full Stack application using permanent free tiers.

**Privacy Note:** We will keep your code in a **Private GitHub Repository**. Render and Vercel can access private repos securely.

---

## 1. Database Setup (TiDB Cloud - Free MySQL)

Since you want a free, permanent MySQL database, we will use **TiDB Cloud Serverless**.

1.  **Sign Up**: Go to [TiDB Cloud](https://tidbcloud.com/) and sign up (free, no credit card needed).
2.  **Create Cluster**: 
    - Create a new "Serverless Tier" cluster.
    - Name it `smart-parking`.
    - Set the region (choose the one closest to you).
    - **Important:** Set a strong root password.
3.  **Get Connection Info**:
    - Once created, click "Connect".
    - Select "Spring Boot" or just "General" to see your parameters.
    - Copy the following details for later:
        - **Host** (e.g., `gateway01.us-east-1.prod.aws.tidbcloud.com`)
        - **Port** (usually `4000`)
        - **Username** (e.g., `2SeE...prefix.root`)
        - **Password** (the one you set)
        - **Database Name** (default is `test`, you can create a new one like `smart_parking_db` using the SQL editor).

---

## 2. Code Security (GitHub)

1.  Go to [GitHub](https://github.com/new).
2.  Create a **New Repository**.
3.  **Important**: Select **Private**.
4.  Push your code to this repository.

---

## 3. Backend Deployment (Render)

Render will build your Java app using the `Dockerfile` we created.

1.  **Sign Up**: Go to [Render](https://render.com/) and sign up using your GitHub account.
2.  **New Web Service**: Click "New +" -> "Web Service".
3.  **Connect Repo**: Select your **Private** `smart-parking` repository.
4.  **Configure Service**:
    - **Name**: `smart-parking-backend`
    - **Runtime**: `Docker` (Render will detect the Dockerfile).
    - **Region**: Choose same as your database if possible.
    - **Instance Type**: Free.
5.  **Environment Variables**:
    - Click "Advanced" or "Environment Variables".
    - Add the following keys (using the values from Step 1):
        - `SPRING_DATASOURCE_URL`: `jdbc:mysql://<TiDB_HOST>:4000/<DB_NAME>?sslMode=VERIFY_IDENTITY&useSSL=true`
        - `SPRING_DATASOURCE_USERNAME`: `<TiDB_USERNAME>`
        - `SPRING_DATASOURCE_PASSWORD`: `<TiDB_PASSWORD>`
6.  **Deploy**: Click "Create Web Service".
    - Render will start building. It might take a few minutes.
    - Once finished, you will get a URL like `https://smart-parking-backend.onrender.com`. **Copy this URL.**

---

## 4. Frontend Deployment (Vercel)

1.  **Sign Up**: Go to [Vercel](https://vercel.com/) and sign up with GitHub.
2.  **Add New Project**: Click "Add New..." -> "Project".
3.  **Import Repo**: Import your **Private** `smart-parking` repository.
4.  **Configure Project**:
    - **Framework Preset**: Create React App (should detect automatically).
    - **Root Directory**: Click "Edit" and select `smart-parking-frontend`.
5.  **Environment Variables**:
    - Expand "Environment Variables".
    - Add:
        - **Key**: `REACT_APP_API_URL`
        - **Value**: `https://smart-parking-backend.onrender.com` (The URL from Step 3).
6.  **Deploy**: Click "Deploy".
    - Vercel will build and deploy your site.
    - You will get a final URL like `https://smart-parking-frontend.vercel.app`.

---

## 5. Verification

1.  Open your Vercel URL.
2.  Try to Log In (ensure you have created a user in your new DB, or use the Signup flow if enabled).
3.  Check if data loads from the backend.

### Troubleshooting
- **Backend logs**: In Render dashboard, click "Logs" to see if the Java app started correctly or if there are DB connection errors.
- **Frontend errors**: Open Chrome Developer Tools (F12) -> Console to see if API calls are failing.
