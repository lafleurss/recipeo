package recipeo.activity.requests;

public class GetCategoriesForUserRequest {
    private final String userId;


    private GetCategoriesForUserRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetCategoriesForUserRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GetCategoriesForUserRequest build() {
            return new GetCategoriesForUserRequest(userId);
        }
    }
}
