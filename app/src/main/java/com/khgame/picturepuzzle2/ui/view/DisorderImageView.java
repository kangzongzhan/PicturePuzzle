package com.khgame.picturepuzzle2.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.core.Point;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import static com.khgame.picturepuzzle.core.GameLevel.xNums;
import static com.khgame.picturepuzzle.core.GameLevel.yNums;

@SuppressLint("DrawAllocation")
public class DisorderImageView extends View {
    private static final String TAG = "DisorderImageView";

    private List<Point> list = null;
    private Bitmap bitmap = null;

    public DisorderImageView( Context context ) {
        super( context );
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        super.onDraw( canvas );
        if ( !isReady() ) {
            Log.d( TAG, "onDraw(), is not ready" );
            return;
        }

        getPaddingLeft();

        int mWith = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int mHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        int unitMWidth = mWith/xNums(list);
        int unitMHeight = mHeight/yNums(list);

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        int unitBWidth = bWidth/xNums(list);
        int unitBHeigth = bHeight/yNums(list);


        List<Point> list = moveEmptyItemToLast(this.list);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        for(int i = 0; i < list.size() - 1; i++){
            Point p = list.get( i );

            // 当前为空， 用最后一个补
            if(p.x == 0 && p.y == yNums(this.list)){
                p = list.get(list.size()-1);
            }

            Rect src = new Rect( p.x * unitBWidth, p.y * unitBHeigth, (p.x+1) * unitBWidth, (p.y+1) * unitBHeigth );

            int x = i % xNums( this.list );
            int y = i / xNums( this.list );
            Rect dst = new Rect( x * unitMWidth + getPaddingLeft(), y * unitMHeight + getPaddingTop(), (x+1) * unitMWidth + getPaddingLeft(), (y+1) * unitMHeight + getPaddingTop() );

            canvas.drawBitmap( bitmap, src, dst, paint );
        }

    }

    public void setPositionList( List<Point> list ) {
        this.list = list;
        if ( isReady() ) {
            invalidate();
        }
    }
    public List<Point> getPositionList(){
        return this.list;
    }

    public void setBitmap( Bitmap bitmap ) {
        this.bitmap = bitmap;
        if ( isReady() ) {
            invalidate();
        }
    }

    // when bitmap and list both ready, image can be draw
    private boolean isReady() {

        if ( this.bitmap == null || this.list == null ) {
            return false;
        }

        int count = this.list.size();
        if (count == 13 || count == 25 || count == 49) {
            return true;
        }
        return false;
    }

    private List<Point> moveEmptyItemToLast( List<Point> list ) {

        if(list == null){
            return null;
        }
        List<Point> tempList = cloneList( list );

        //int gameLevel = gameLevel( tempList );
        //int xnums = xNums( tempList );
        int ynums = yNums( tempList );

        Point emptyItem = new Point(0, ynums + 1);

        while ( true ) {
            Point position = leftOf( tempList, emptyItem );
            if(position == null){
                break;
            }
            swap( tempList, emptyItem, position );
        }
        while( true ){
            Point position = bottomOf( tempList, emptyItem );
            if(position == null){
                break;
            }
            swap( tempList, emptyItem, position );
        }
        return tempList;
    }

    // return EASY MEDIUM HARD game level
    // return -1 when list is invalide
    private int gameLevel( List<Point> list ) {
        if ( list == null ) {
            return -1;
        }
        if ( list.size() == 3 * 4 + 1 ) {
            return GameLevel.EASY;
        }
        if ( list.size() == 4 * 6 + 1 ) {
            return GameLevel.MEDIUM;
        }
        if ( list.size() == 6 * 8 + 1 ) {
            return GameLevel.HARD;
        }
        return -1;
    }

    // return left position of the given position in the list
    private Point leftOf( List<Point> list, Point position ) {
        if(list == null || position == null){
            return null;
        }
        int xnums = xNums( list );

        int index = list.indexOf( position );
        if(index == -1){
            return null;
        }

        int x = index % xnums;

        if(x == 0){
            return null;
        }
        return list.get( index-1 );
    }

    // return right position of the given position in the list
    private Point rightOf( List<Point> list, Point position ) {
        if(list == null || position == null){
            return null;
        }
        int xnums = xNums( list );
        int ynums = yNums( list );

        int index = list.indexOf( position );
        if(index == -1){
            return null;
        }

        int x = index % xnums;
        int y = index % xnums;

        if(x == xnums-1 || y == ynums){
            return null;
        }
        return list.get( index+1 );
    }

    // return bottom position of the given position in the list
    private Point bottomOf( List<Point> list, Point position ) {
        if(list == null || position == null){
            return null;
        }
        int xnums = xNums( list );
        int ynums = yNums( list );

        int index = list.indexOf( position );
        if(index == -1){
            return null;
        }

        int x = index % xnums;
        int y = index % xnums;


        if(x == 0 && y == ynums){
            return null;
        }
        if( x > 0 && y == ynums -1){
            return null;
        }
        return list.get( index+xnums );
    }

    // return top position of the given position in the list
    private Point topOf( List<Point> list, Point position ) {
        if(list == null || position == null){
            return null;
        }
        int xnums = xNums( list );
        int ynums = yNums( list );

        int index = list.indexOf( position );
        if(index == -1){
            return null;
        }

        int x = index % xnums;
        int y = index % xnums;

        if(y == 0){
            return null;
        }
        return list.get( index - xnums );
    }

    public void swap(List<Point> list, Point p1, Point p2){

        if(list == null || p1 == null || p2 == null){
            Log.e( TAG, "list, p1, p2 can not be null" );
            return;
        }
        if(!list.contains( p1 ) || !list.contains( p2 )){
            Log.e( TAG, "p1 or p2 is not contains in list" );
            return;
        }

        int indexP1 = list.indexOf( p1 );
        int indexP2 = list.indexOf( p2 );

        list.add( indexP1, p2 );
        list.add( indexP2, p1 );

    }
    public List<Point> cloneList(List<Point> list){

        if(list == null){
            return null;
        }

        List<Point> tempList = new ArrayList<Point>();
        for ( Point position : list ) {
            Point p = new Point( position.x, position.y );
            tempList.add( p );
        }
        return tempList;
    }

}
