package recipeo.activity.requests;

public class SearchRecipesRequest {
    private final String criteria;

    private SearchRecipesRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchRecipesRequest{" +
                "criteria='" + criteria + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String criteria;

        public Builder withCriteria(String criteria) {
            this.criteria = criteria;
            return this;
        }

        public SearchRecipesRequest build() {
            return new SearchRecipesRequest(criteria);
        }
    }
}
