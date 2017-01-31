package realm;

import android.content.Context;
import android.widget.Toast;

import io.realm.Realm;
import model.MainActivityPojo;
import model.MovieBookmarkModel;

/**
 * Created by abhinav on 04-01-2017.*/



public class AddBookmark {

    public AddBookmark(){};

    public static void add(final Context context, final MainActivityPojo list) {
        Realm realm=Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm bgrealm) {
                                              MainActivityPojo adb = bgrealm.createObject(MainActivityPojo.class,list.getMovieid());
                                              adb.setCardtitle(list.getCardtitle());
                                              adb.setTimestamp(list.getTimestamp());
                                              adb.setContent(list.getContent());
                                              final String imagrUrl="http://image.tmdb.org/t/p/w342"+list.getImagepath();
                                              adb.setImagepath(imagrUrl);
                                              adb.setBackPoster(list.getBackPoster());
                                              adb.setVoteCount(list.getVoteCount());
                                              adb.setShareurl(list.getShareurl());

                                          }
                                      }, new Realm.Transaction.OnSuccess() {
                                          @Override
                                          public void onSuccess() {
                                              Toast.makeText(context,"added",Toast.LENGTH_SHORT).show();

                                          }
                                      }, new Realm.Transaction.OnError() {
                                          @Override
                                          public void onError(Throwable error) {
                                              error.printStackTrace();
                                              Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                                          }
                                      });
    }

}
