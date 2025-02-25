const BASE_URL = "http://localhost:8080/upload";

export const uploadData = async (fileData) => {
    try {
        const response = await fetch(BASE_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(fileData),
        });

        return await response.json();
    } catch (error) {
        console.error("Upload Error:", error);
        return { status: "error", message: "Server error" };
    }
};
