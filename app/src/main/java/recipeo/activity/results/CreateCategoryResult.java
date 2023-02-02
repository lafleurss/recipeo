package recipeo.activity.results;

import recipeo.dynamodb.models.Category;

public class CreateCategoryResult {

    private Category category;

    private CreateCategoryResult(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "CreateCategoryResult{" +
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

        public CreateCategoryResult build() {
            return new CreateCategoryResult(category);
        }
    }
}
