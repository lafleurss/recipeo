package recipeo.activity.requests;

public class CreateCategoryRequest {

    private String userId;
    private String categoryName;
    private String categoryDescription;

    private CreateCategoryRequest(String userId, String categoryName, String categoryDescription) {
        this.categoryName = categoryName;
        this.userId = userId;
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    @Override
    public String toString() {
        return "CreateCategoryRequest{" +
                ", userId='" + userId + '\'' +
                "categoryName='" + categoryName + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String categoryName;
        private String userId;
        private String categoryDescription;

        public Builder withCategoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCategoryDescription(String categoryDescription) {
            this.categoryDescription = categoryDescription;
            return this;
        }

        public CreateCategoryRequest build() {
            return new CreateCategoryRequest(userId, categoryName, categoryDescription);
        }
    }
}
