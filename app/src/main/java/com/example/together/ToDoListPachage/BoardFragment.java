/*
 * Copyright 2014 Magnus Woxblom
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.together.ToDoListPachage;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.together.R;
import com.example.together.data.model.ListTask;
import com.example.together.data.storage.Storage;
import com.example.together.utils.HelperClass;
import com.example.together.view_model.UserViewModel;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.ColumnProperties;
import com.woxthebox.draglistview.DragItem;

import java.util.ArrayList;
import java.util.Objects;

public class BoardFragment extends Fragment {

    private static int sCreatedItems = 0;
    public BoardView mBoardView;
    public Button addTask;
    public ProgressBar b;
    public TextView percentageView;
    int data = 50;
    ItemAdapter toDoListAdapter;
    ItemAdapter doingListAdapter;
    ItemAdapter doneListAdapter;
    UserViewModel userViewModel;
    ArrayList<ListTask> toDoList = new ArrayList<>();
    ArrayList<ListTask> doingList = new ArrayList<>();
    ArrayList<ListTask> doneList = new ArrayList<>();
    Storage storage;
    Storage storageForUserID;
    HandleViewModelProcess handleViewModelProcess;
    GetAddTaskButton getAddTaskButton;
    private int mColumns;
    private boolean mGridLayout;
    CreateDialog dialog;
    boolean isAdmin = false;
    CreateDialog adapterDialog;


    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        storage = new Storage(Objects.requireNonNull(getContext()));
        storageForUserID=new Storage();
        handleViewModelProcess = new HandleViewModelProcess(userViewModel, this);
        checkIsAdmin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_layout, container, false);

        mBoardView = view.findViewById(R.id.board_view);
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setSnapToColumnInLandscape(false);
        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {

            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                Log.i("mahmoud", "onItemDragEnded: " + toDoList.size() + " ");
                if (HelperClass.checkInternetState(getContext())) {
                    if (fromColumn == 0 && toColumn == 1) {
                        handleViewModelProcess.moveToProgressList(BoardFragment.this.doingList.get(toRow).getId());
//                        doingList.get(toRow).setPosition(toRow);
                        rearrangeList(toDoList);
                        rearrangeList(doingList);
                        toArrangeList(toDoList, doingList);

                    }
                    if (fromColumn == 0 && toColumn == 2) {
                        handleViewModelProcess.moveToDoneList(BoardFragment.this.doneList.get(toRow).getId());
//                        doneList.get(toRow).setPosition(toRow);
                        rearrangeList(toDoList);
                        rearrangeList(doneList);
                        toArrangeList(toDoList, doneList);

                    }
                    if (fromColumn == 1 && toColumn == 0) {
                        handleViewModelProcess.moveToDoList(BoardFragment.this.toDoList.get(toRow).getId());
//                        toDoList.get(toRow).setPosition(toRow);
                        rearrangeList(doingList);
                        rearrangeList(toDoList);
                        toArrangeList(doingList, toDoList);

                    }
                    if (fromColumn == 1 && toColumn == 2) {
                        handleViewModelProcess.moveToDoneList(BoardFragment.this.doneList.get(toRow).getId());
//                        doneList.get(toRow).setPosition(toRow);
                        rearrangeList(doingList);
                        rearrangeList(doneList);
                        toArrangeList(doingList, doneList);

                    }
                    if (fromColumn == 2 && toColumn == 0) {
                        handleViewModelProcess.moveToDoList(BoardFragment.this.toDoList.get(toRow).getId());
//                        toDoList.get(toRow).setPosition(toRow);
                        rearrangeList(doneList);
                        rearrangeList(toDoList);
                        toArrangeList(doneList, toDoList);


                    }
                    if (fromColumn == 2 && toColumn == 1) {
                        handleViewModelProcess.moveToProgressList(BoardFragment.this.doingList.get(toRow).getId());
//                        doingList.get(toRow).setPosition(toRow);
                        rearrangeList(doneList);
                        rearrangeList(doingList);
                        toArrangeList(doneList, doingList);
                    }
                    if (fromColumn == 0 && toColumn == 0) {
                        rearrangeList(toDoList);
                        handleViewModelProcess.sendPositionArrangment(toDoList);
                    }
                    if (fromColumn == 1 && toColumn == 1) {
                        rearrangeList(doingList);
                        handleViewModelProcess.sendPositionArrangment(doingList);
                    }
                    if (fromColumn == 2 && toColumn == 2) {
                        rearrangeList(doneList);
                        handleViewModelProcess.sendPositionArrangment(doneList);
                    }
                }

            }

            @Override
            public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {


            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                TextView itemCount1 = mBoardView.getHeaderView(oldColumn).findViewById(R.id.item_count);
                itemCount1.setText(String.valueOf(mBoardView.getAdapter(oldColumn).getItemCount()));
                TextView itemCount2 = mBoardView.getHeaderView(newColumn).findViewById(R.id.item_count);
                itemCount2.setText(String.valueOf(mBoardView.getAdapter(newColumn).getItemCount()));
                Log.i("mahmoud", "onItemChangedColumn: " + toDoList.size());

            }

            @Override
            public void onFocusedColumnChanged(int oldColumn, int newColumn) {
                Log.i("mahmoud", "onFocusedColumnChanged: " + toDoList.size());
            }

            @Override
            public void onColumnDragStarted(int position) {
                Log.i("mahmoud", "onColumnDragStarted: " + toDoList.size());
            }

            @Override
            public void onColumnDragChangedPosition(int oldPosition, int newPosition) {
                Log.i("mahmoud", "onColumnDragChangedPosition: " + toDoList.size());
            }

            @Override
            public void onColumnDragEnded(int position) {
                Log.i("mahmoud", "onColumnDragEnded: " + toDoList.size());

            }
        });
        if(isAdmin) {
            mBoardView.setBoardCallback(new BoardView.BoardCallback() {
                @Override
                public boolean canDragItemAtPosition(int column, int dragPosition) {
                    if (HelperClass.checkInternetState(getContext()))
                        return true;
                    else {
                        HelperClass.showAlert("Error", "Please check your internet connection", getContext());

                        return false;
                    }
                }

                @Override
                public boolean canDropItemAtPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
                    return true;
                }
            });
        }
        else {
            mBoardView.setBoardCallback(new BoardView.BoardCallback() {
                @Override
                public boolean canDragItemAtPosition(int column, int dragPosition) {
                        return false;
                }

                @Override
                public boolean canDropItemAtPosition(int oldColumn, int oldRow, int newColumn, int newRow) {
                    return false;
                }
            });
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Board");
        getAddTaskButton = (GetAddTaskButton) getActivity();
        addTask = getAddTaskButton.getAddTask();
        b = getAddTaskButton.getProgressBar();
        percentageView = getAddTaskButton.getPercentageView();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperClass.checkInternetState(getContext())) {
                    dialog = new CreateDialog("addTask", BoardFragment.this);
                    dialog.show(((FragmentActivity) BoardFragment.this.getContext()).getSupportFragmentManager(), "example");
                } else {
                    HelperClass.showAlert("Error", "Please check your internet connection", getContext());

                }

            }
        });
        resetBoard();
        handleViewModelProcess.getToDoListTask();
        handleViewModelProcess.getInProgressTasks();
        handleViewModelProcess.getDoneTasks();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_board, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_disable_drag).setVisible(mBoardView.isDragEnabled());
        menu.findItem(R.id.action_enable_drag).setVisible(!mBoardView.isDragEnabled());
        menu.findItem(R.id.action_grid).setVisible(!mGridLayout);
        menu.findItem(R.id.action_list).setVisible(mGridLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_disable_drag:
                mBoardView.setDragEnabled(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_enable_drag:
                mBoardView.setDragEnabled(true);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_grid:
                mGridLayout = true;
                resetBoard();
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_list:
                mGridLayout = false;
                resetBoard();
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_add_column:
//                addColumn("ay7aga",);
                return true;
            case R.id.action_remove_column:
                mBoardView.removeColumn(0);
                return true;
            case R.id.action_clear_board:
                mBoardView.clearBoard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetBoard() {
        mBoardView.clearBoard();
        mBoardView.setCustomDragItem(mGridLayout ? null : new MyDragItem(getActivity(), R.layout.column_item));
        mBoardView.setCustomColumnDragItem(mGridLayout ? null : new MyColumnDragItem(getActivity(), R.layout.column_drag_layout));

        addColumn("To Do List", toDoList);
        addColumn("Doing List", doingList);
        addColumn("Done List", doneList);
    }


    private void addColumn(String columnName, final ArrayList<ListTask> mItemArray) {
        ItemAdapter listAdapter;
        if (columnName.equals("To Do List")) {
            listAdapter = new ItemAdapter(mItemArray, mGridLayout ? R.layout.grid_item : R.layout.column_item, R.id.item_layout, true, this.getContext(), this, 0);
            this.toDoListAdapter = listAdapter;
        } else if (columnName.equals("Doing List")) {
            listAdapter = new ItemAdapter(mItemArray, mGridLayout ? R.layout.grid_item : R.layout.column_item, R.id.item_layout, true, this.getContext(), this, 1);
            this.doingListAdapter = listAdapter;
        } else {
            listAdapter = new ItemAdapter(mItemArray, mGridLayout ? R.layout.grid_item : R.layout.column_item, R.id.item_layout, true, this.getContext(), this, 2);

            this.doneListAdapter = listAdapter;
        }
        final View header = View.inflate(getActivity(), R.layout.column_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(columnName);
        ((TextView) header.findViewById(R.id.item_count)).setText("" + mItemArray.size());

        LinearLayoutManager layoutManager = mGridLayout ? new GridLayoutManager(getContext(), 4) : new LinearLayoutManager(getContext());
        ColumnProperties columnProperties = ColumnProperties.Builder.newBuilder(listAdapter)
                .setLayoutManager(layoutManager)
                .setHasFixedItemSize(false)
                .setColumnBackgroundColor(Color.TRANSPARENT)
                .setItemsSectionBackgroundColor(Color.TRANSPARENT)
                .setHeader(header)
                .setColumnDragView(header)
                .build();

        mBoardView.addColumn(columnProperties);
        mColumns++;
    }


    public void addTask(ListTask task) {
        handleViewModelProcess.addTask(task);
    }

    public void toArrangeList(ArrayList<ListTask> l1, ArrayList<ListTask> l2) {
        ArrayList<ListTask> list = new ArrayList<>(l1.size() + l2.size());
        list.addAll(l1);
        list.addAll(l2);
        handleViewModelProcess.sendPositionArrangment(list);
    }

    public void rearrangeList(ArrayList<ListTask> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPosition(i);
        }
    }

    private static class MyColumnDragItem extends DragItem {

        MyColumnDragItem(Context context, int layoutId) {
            super(context, layoutId);
            setSnapToTouch(false);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            LinearLayout clickedLayout = (LinearLayout) clickedView;
            View clickedHeader = clickedLayout.getChildAt(0);
            RecyclerView clickedRecyclerView = (RecyclerView) clickedLayout.getChildAt(1);

            View dragHeader = dragView.findViewById(R.id.drag_header);
            ScrollView dragScrollView = dragView.findViewById(R.id.drag_scroll_view);
            LinearLayout dragLayout = dragView.findViewById(R.id.drag_list);

            Drawable clickedColumnBackground = clickedLayout.getBackground();
            if (clickedColumnBackground != null) {
                ViewCompat.setBackground(dragView, clickedColumnBackground);
            }

            Drawable clickedRecyclerBackground = clickedRecyclerView.getBackground();
            if (clickedRecyclerBackground != null) {
                ViewCompat.setBackground(dragLayout, clickedRecyclerBackground);
            }

            dragLayout.removeAllViews();

            ((TextView) dragHeader.findViewById(R.id.text)).setText(((TextView) clickedHeader.findViewById(R.id.text)).getText());
            ((TextView) dragHeader.findViewById(R.id.item_count)).setText(((TextView) clickedHeader.findViewById(R.id.item_count)).getText());
            for (int i = 0; i < clickedRecyclerView.getChildCount(); i++) {
                View view = View.inflate(dragView.getContext(), R.layout.column_item, null);
                ((TextView) view.findViewById(R.id.text)).setText(((TextView) clickedRecyclerView.getChildAt(i).findViewById(R.id.text)).getText());
                dragLayout.addView(view);

                if (i == 0) {
                    dragScrollView.setScrollY(-clickedRecyclerView.getChildAt(i).getTop());
                }
            }

            dragView.setPivotY(0);
            dragView.setPivotX(clickedView.getMeasuredWidth() / 2);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            super.onStartDragAnimation(dragView);
            dragView.animate().scaleX(0.9f).scaleY(0.9f).start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            super.onEndDragAnimation(dragView);
            dragView.animate().scaleX(1).scaleY(1).start();
        }
    }

    private static class MyDragItem extends DragItem {

        MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.task_title)).getText();
            ((TextView) dragView.findViewById(R.id.task_title)).setText(text);
            CharSequence text2 = ((TextView) clickedView.findViewById(R.id.task_description)).getText();
            ((TextView) dragView.findViewById(R.id.task_description)).setText(text2);
            CardView dragCard = dragView.findViewById(R.id.card);
            CardView clickedCard = clickedView.findViewById(R.id.card);

            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            // I know the dragView is a FrameLayout and that is why I can use setForeground below api level 23
            dragCard.setForeground(clickedView.getResources().getDrawable(R.drawable.card_view_drag_foreground));
        }

        @Override
        public void onMeasureDragView(View clickedView, View dragView) {
            CardView dragCard = dragView.findViewById(R.id.card);
            CardView clickedCard = clickedView.findViewById(R.id.card);
            int widthDiff = dragCard.getPaddingLeft() - clickedCard.getPaddingLeft() + dragCard.getPaddingRight() -
                    clickedCard.getPaddingRight();
            int heightDiff = dragCard.getPaddingTop() - clickedCard.getPaddingTop() + dragCard.getPaddingBottom() -
                    clickedCard.getPaddingBottom();
            int width = clickedView.getMeasuredWidth() + widthDiff;
            int height = clickedView.getMeasuredHeight() + heightDiff;
            dragView.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            dragView.measure(widthSpec, heightSpec);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            CardView dragCard = dragView.findViewById(R.id.card);
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 40);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = dragView.findViewById(R.id.card);
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 6);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
        if(adapterDialog!=null){
            adapterDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       checkIsAdmin();
    }

    public void checkIsAdmin(){
        int adminID=storageForUserID.getGroup(getContext()).getAdminID();
        int userId = storage.getId();
        if(adminID==userId){
            isAdmin=true;
        }
    }
}
