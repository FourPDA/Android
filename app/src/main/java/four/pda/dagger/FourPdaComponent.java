package four.pda.dagger;

import dagger.Component;
import four.pda.ui.DrawerFragment;
import four.pda.ui.article.comments.CommentsFragment;
import four.pda.ui.article.list.ListFragment;
import four.pda.ui.article.one.ArticleFragment;
import four.pda.ui.article.one.ArticleTaskLoader;
import four.pda.ui.auth.AuthActivity;

/**
 * Created by asavinova on 23/02/16.
 */
@Component(modules = {ClientModule.class})
public interface FourPdaComponent {

	void inject(ListFragment x);

	void inject(ArticleFragment x);

	void inject(ArticleTaskLoader x);

	void inject(CommentsFragment x);

	void inject(AuthActivity x);

	void inject(DrawerFragment x);

}
