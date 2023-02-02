package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetCategoriesForUserRequest;
import recipeo.activity.results.GetCategoriesForUserResult;

public class GetCategoriesForUserLambda
        extends LambdaActivityRunner<GetCategoriesForUserRequest, GetCategoriesForUserResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetCategoriesForUserRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetCategoriesForUserRequest> input,
                                        Context context) {
        log.info("handleRequest");

        return super.runActivity(() -> input.fromUserClaims(claims  ->
                        GetCategoriesForUserRequest.builder()
                                .withUserId(claims.get("email"))
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideGetCategoriesForUserActivity().handleRequest(request)
        );
    }

}
