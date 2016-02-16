package four.pda;

import org.androidannotations.annotations.EBean;

/**
 * Created by asavinova on 15/02/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class FourPdaClient {

	private four.pda.client.FourPdaClient client = new four.pda.client.FourPdaClient();

	public four.pda.client.FourPdaClient getInstance() {
		return client;
	}
}
