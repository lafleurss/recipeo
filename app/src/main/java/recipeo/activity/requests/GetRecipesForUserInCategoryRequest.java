package recipeo.activity.requests;

import recipeo.activity.results.GetRecipesForUserInCategoryResult;
import recipeo.dynamodb.models.Category;
import recipeo.models.RecipeFilter;

public class GetRecipesForUserInCategoryRequest {
    private final String userId;
    private final String categoryName;

    /**
     * Get an instance of GetRecipesForUserInCategoryRequest.
     * @param userId userId to request recipes for
     * @param categoryName category type to be displayed
     */
    public GetRecipesForUserInCategoryRequest(String userId, String categoryName) {
        this.userId = userId;
        this.categoryName = categoryName;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategory() {
        return categoryName;
    }

    @Override
    public String toString() {
        return "GetRecipesForUserInCategoryRequest{" +
                "userId='" + userId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

//CHECKSTYLE:OFF:Builder

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private String userId;
        private String categoryName;

         public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCategoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public GetRecipesForUserInCategoryRequest build() {
            return new GetRecipesForUserInCategoryRequest(userId, categoryName);
        }
    }
}
