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
import four.pda.client.model.Comment;
import four.pda.client.model.CommentsContainer;
import four.pda.client.model.DeletedComment;

/**
 * Created by asavinova on 05/12/15.
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final static int NORMAL_COMMENT_TYPE = 0;
	private final static int DELETED_COMMENT_TYPE = 1;
	private final static int ADD_COMMENT_TYPE = 2;

	private final LayoutInflater inflater;
	private List<AbstractComment> comments = new ArrayList<>();
	private boolean canAddNewComment = false;
	private int viewWidth;

	public CommentsAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == ADD_COMMENT_TYPE) {
			View view = inflater.inflate(R.layout.add_comment_item, parent, false);
			return new AddCommentViewHolder(view);
		}

		if (viewType == DELETED_COMMENT_TYPE) {
			View view = inflater.inflate(R.layout.deleted_comment_item, parent, false);
			return new RecyclerView.ViewHolder(view) {};
		}

		View view = inflater.inflate(R.layout.comment_item, parent, false);
		return new CommentViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		int type = getItemViewType(position);

		if (type == ADD_COMMENT_TYPE) {
			return;
		}

		AbstractComment abstractComment = comments.get(position);

		if (type == NORMAL_COMMENT_TYPE) {
			((CommentViewHolder) holder).setComment((Comment) abstractComment);
		}

		int left = viewWidth / 30 * abstractComment.getLevel();
		holder.itemView.setPadding(left, 0, 0, 0);
	}

	@Override
	public int getItemCount() {
		return comments.size() + (canAddNewComment ? 1 : 0);
	}

	@Override
	public int getItemViewType(int position) {
		if (position >= comments.size()) {
			return ADD_COMMENT_TYPE;
		}

		if (comments.get(position) instanceof DeletedComment) {
			return DELETED_COMMENT_TYPE;
		}
		return NORMAL_COMMENT_TYPE;
	}

	public void setViewWidth(int width) {
		this.viewWidth = width;
	}

	public void setCommentsContainer(CommentsContainer container) {
		this.comments = new ArrayList<>();

		canAddNewComment = container.canAddNewComment();
		addComments(container.getComments());
	}

	private void addComments(List<AbstractComment> tree) {

		if (tree == null) return;

		for (AbstractComment comment : tree) {
			comments.add(comment);
			addComments(comment.getChildren());
		}
	}

}
