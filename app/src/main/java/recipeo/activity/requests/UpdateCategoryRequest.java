package recipeo.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = UpdateCategoryRequest.Builder.class)
public class UpdateCategoryRequest {
    private String userId;
    private String categoryName;
    private String categoryDescription;


    private UpdateCategoryRequest(String userId, String categoryName, String categoryDescription) {
        this.userId = userId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    @Override
    public String toString() {
        return "UpdateCategoryRequest{" +
                "userId='" + userId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
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

        public UpdateCategoryRequest build() {
            return new UpdateCategoryRequest(userId, categoryName, categoryDescription);
        }
    }
}
