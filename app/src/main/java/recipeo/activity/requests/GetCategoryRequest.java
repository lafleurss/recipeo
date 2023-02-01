package recipeo.activity.requests;

public class GetCategoryRequest {
    private final String categoryName;
    private final String userId;

    private GetCategoryRequest(String userId, String categoryName) {
        this.categoryName = categoryName;
        this.userId = userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetCategoryRequest{" +
                "categoryName='" + categoryName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String categoryName;
        private String userId;

        public Builder withCategoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GetCategoryRequest build() {
            return new GetCategoryRequest(userId, categoryName);
        }
    }
}
