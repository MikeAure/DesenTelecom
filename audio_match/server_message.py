class ServerResponse:
    def __init__(self):
        self.status = "ok"
        self.message = ""

    def __str__(self):
        return f"status: {self.status}, message: {self.message}"