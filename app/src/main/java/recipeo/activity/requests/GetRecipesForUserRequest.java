package recipeo.activity.requests;

public class GetRecipesForUserRequest {
    private final String userId;


    /**
     * Get an instance of GetRecipesForUserRequest.
     * @param userId userId to request recipes for
     */
    public GetRecipesForUserRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetRecipesForUserRequest{" +
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

        public GetRecipesForUserRequest build() {
            return new GetRecipesForUserRequest(userId);
        }
    }
}
