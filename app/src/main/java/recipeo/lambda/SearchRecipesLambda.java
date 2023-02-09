package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.SearchRecipesRequest;
import recipeo.activity.results.SearchRecipesResult;

public class SearchRecipesLambda
        extends LambdaActivityRunner<SearchRecipesRequest, SearchRecipesResult>
        implements RequestHandler<LambdaRequest<SearchRecipesRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<SearchRecipesRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(() -> input.fromQuery(query ->
                        SearchRecipesRequest.builder()
                                .withCriteria(query.get("q"))
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideSearchPlaylistsActivity().handleRequest(request)
        );
    }
}

