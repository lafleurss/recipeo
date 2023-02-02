package recipeo.activity.results;

import recipeo.dynamodb.models.Category;

public class UpdateCategoryResult {
    private Category category;

    private UpdateCategoryResult(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "UpdateCategoryResult{" +
                "category=" + category +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private Category category;

        public Builder withCategory(Category category) {
            this.category = category;
            return this;
        }

        public UpdateCategoryResult build() {
            return new UpdateCategoryResult(category);
        }
    }
}
