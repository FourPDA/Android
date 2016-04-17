package four.pda.ui.article.comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import four.pda.R;
import four.pda.client.model.AbstractComment;

/**
 * Created by asavinova on 05/12/15.
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final static int COMMENT_TYPE = 0;
	private final static int ADD_COMMENT_TYPE = 1;

	private final LayoutInflater inflater;
	private List<AbstractComment> comments = new ArrayList<>();

	public CommentsAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == ADD_COMMENT_TYPE) {
			View view = inflater.inflate(R.layout.add_comment_item, parent, false);
			return new AddCommentViewHolder(view);
		}

		View view = inflater.inflate(R.layout.comment_item, parent, false);
		return new CommentViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (getItemViewType(position) == COMMENT_TYPE) {
			((CommentViewHolder) holder).setComment(comments.get(position));
		}
	}

	@Override
	public int getItemCount() {
		return comments.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position < comments.size()) {
			return COMMENT_TYPE;
		}
		return ADD_COMMENT_TYPE;
	}

	public void setComments(List<AbstractComment> tree) {
		this.comments = new ArrayList<>();

		addComments(tree);
	}

	private void addComments(List<AbstractComment> tree) {

		if (tree == null) return;

		for (AbstractComment comment : tree) {
			comments.add(comment);
			addComments(comment.getChildren());
		}
	}
}
