package recipeo.activity.requests;

public class GetRecipeRequest {
    private final String recipeId;
    private final String userId;

    private GetRecipeRequest(String userId, String recipeId) {
        this.recipeId = recipeId;
        this.userId = userId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetRecipeRequest{" +
                "id='" + recipeId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String recipeId;
        private String userId;

        public Builder withRecipeId(String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GetRecipeRequest build() {
            return new GetRecipeRequest(userId, recipeId);
        }
    }

}
