package four.pda.ui.article.comments;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import four.pda.client.FourPdaClient;
import four.pda.ui.LoadResult;

/**
 * Created by pavel on 06/06/16.
 */
class LikeCommentLoader extends AsyncTaskLoader<LoadResult<Void>> {

    private FourPdaClient client;

    private final long commentId;
    private final long articleId;

    public LikeCommentLoader(Context context, FourPdaClient client, long articleId, long commentId) {
        super(context);
        this.client = client;
        this.commentId = commentId;
        this.articleId = articleId;
    }

    @Override
    public LoadResult<Void> loadInBackground() {
        try {
            client.likeArticleComment(articleId, commentId);
            return new LoadResult<>((Void) null);
        } catch (Exception e) {
            return new LoadResult<>(e);
        }
    }

}
