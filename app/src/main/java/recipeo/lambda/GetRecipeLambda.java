package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetRecipeRequest;
import recipeo.activity.results.GetRecipeResult;

public class GetRecipeLambda extends LambdaActivityRunner<GetRecipeRequest, GetRecipeResult>
    implements RequestHandler<AuthenticatedLambdaRequest<GetRecipeRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetRecipeRequest> input, Context context) {
        log.info("Received GetRecipeLambda {}" , input);

        GetRecipeRequest unauthenticatedRequest = input.fromPath(i ->
                GetRecipeRequest.builder()
                        .withRecipeId(i.get("recipeId"))
                        .build());


        return super.runActivity(() -> input.fromUserClaims(claims ->
                        GetRecipeRequest.builder()
                                .withRecipeId(unauthenticatedRequest.getRecipeId())
                                .withUserId(claims.get("email"))
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideGetRecipeActivity().handleRequest(request)
        );
    }
}
