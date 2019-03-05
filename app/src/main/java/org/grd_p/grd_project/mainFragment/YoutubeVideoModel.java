package org.grd_p.grd_project.mainFragment;

public class YoutubeVideoModel {
    private String videoID, Title, viewNum, PostedTime;

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getViewNum() {
        return viewNum;
    }

    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }

    public String getPostedTime() {
        return PostedTime;
    }

    public void setPostedTime(String postedTime) {
        PostedTime = postedTime;
    }

    @Override
    public String toString() {
        return "YoutubeVideoModel{"+
                "videoID='"+videoID+'\''+
                ", title='"+Title+'\''+
                ", viewNum='"+viewNum+'\''+
                ", postedTime='"+PostedTime+'\''+
                '}';
    }
}
