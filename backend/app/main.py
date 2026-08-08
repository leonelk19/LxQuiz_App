from fastapi import FastAPI
from strawberry.fastapi import GraphQLRouter
from app.graphql.schema import schema

app = FastAPI(
    title="Lxquiz Backend API",
    description="Backend for Lxquiz with GraphQL & Real-time Competition",
    version="1.0.0"
)

graphql_app = GraphQLRouter(schema)

# Routes
app.include_router(graphql_app, prefix="/graphql")

@app.get("/")
async def root():
    return {"message": "Welcome to Lxquiz Backend API. GraphQL & LAC Active."}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
