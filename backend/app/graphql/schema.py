import strawberry
from typing import List, Optional
from datetime import datetime

@strawberry.type
class UserProfile:
    uid: strawberry.ID
    full_name: str
    email: str
    points: int
    streak: int
    education_level: Optional[str]

@strawberry.type
class Tournament:
    id: strawberry.ID
    title: str
    status: str

@strawberry.type
class LeaderboardEntry:
    user_id: strawberry.ID
    user_name: str
    rank: int
    points: int
    country_id: str

@strawberry.type
class IntegrityResponse:
    status: str
    action: Optional[str]

@strawberry.type
class ArenaRoom:
    room_id: strawberry.ID
    status: str

@strawberry.input
class IntegrityEventInput:
    event_type: str
    session_id: str
    details: Optional[str] = None

@strawberry.input
class ArenaConfigInput:
    subject_id: str
    question_count: int

@strawberry.type
class Query:
    @strawberry.field
    def user_profile(self, uid: str) -> Optional[UserProfile]:
        # Implementation for user profile fetching
        return UserProfile(
            uid=strawberry.ID(uid),
            full_name="Alex Ngar",
            email="test@example.com",
            points=1200,
            streak=5,
            education_level="SECONDARY"
        )

    @strawberry.field
    def tournaments(self) -> List[Tournament]:
        return [
            Tournament(id=strawberry.ID("1"), title="Science Olympiad", status="ONGOING"),
            Tournament(id=strawberry.ID("2"), title="Math Whiz", status="UPCOMING")
        ]

@strawberry.type
class Mutation:
    @strawberry.mutation
    def report_integrity_event(self, input: IntegrityEventInput) -> IntegrityResponse:
        print(f"LAC ALERT: {input.event_type} in session {input.session_id}")
        return IntegrityResponse(status="recorded", action="none")

    @strawberry.mutation
    def create_arena_room(self, config: ArenaConfigInput) -> ArenaRoom:
        return ArenaRoom(room_id=strawberry.ID("ARENA123"), status="WAITING")

schema = strawberry.Schema(query=Query, mutation=Mutation)
