package recipeo.activity.requests;

import recipeo.models.RecipeFilter;

public class GetRecipesForUserRequest {
    private final String userId;
    private final RecipeFilter filterType;

    /**
     * Get an instance of GetRecipesForUserRequest.
     * @param userId userId to request recipes for
     * @param filterType filter type to show ALL, FAVORITES or RECENTLY USED
     */
    public GetRecipesForUserRequest(String userId, RecipeFilter filterType) {
        this.userId = userId;
        this.filterType = filterType;
    }

    public String getUserId() {
        return userId;
    }

    public RecipeFilter getFilterType() {
        return filterType;
    }

    @Override
    public String toString() {
        return "GetRecipesForUserRequest{" +
                "userId='" + userId + '\'' +
                ", filterType='" + filterType + '\'' +
                '}';
    }

//CHECKSTYLE:OFF:Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private RecipeFilter filterType;

         public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withFilterType(RecipeFilter filterType) {
            this.filterType = filterType;
            return this;
        }

        public GetRecipesForUserRequest build() {
            return new GetRecipesForUserRequest(userId, filterType);
        }
    }
}
