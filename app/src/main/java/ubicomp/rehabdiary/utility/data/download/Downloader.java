package ubicomp.rehabdiary.utility.data.download;

import android.os.AsyncTask;

import ubicomp.rehabdiary.MainActivity;
import ubicomp.rehabdiary.utility.data.db.DatabaseControl;
import ubicomp.rehabdiary.utility.data.structure.Cassette;

/**
 * Created by yu on 2016/7/6.
 */
public class Downloader {
    DatabaseControl db = null;

    private TriggerListCollect triggerListCollect = null;
    private SvmModelDownloader svmModelDownloader = null;
    private Cassette[] cassettes;
    private CassetteIDCollector cassetteCollector = null;


    public Downloader(){
        db = new DatabaseControl();
    }

    // check SVN version and download model from server
    public void updateSVM(){
        UpdateSvmTask updateSvmTask = new UpdateSvmTask();
        updateSvmTask.execute();
    }

    // check TriggerList version and download list from server
    public void updateTrigger(){
        UpdateTriggerTask updateTriggerTask = new UpdateTriggerTask();
        updateTriggerTask.execute();
    }

    // download Cassette list from server and update phone database
    public void updateCassetteTask(){
        UpdateCassetteTask updateCassetteTask = new UpdateCassetteTask();
        updateCassetteTask.execute();
    }


    private class UpdateSvmTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            svmModelDownloader = new SvmModelDownloader(MainActivity.getMainActivity());
            svmModelDownloader.update();
            return null;
        }

    }

    private class UpdateCassetteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            cassetteCollector = new CassetteIDCollector(MainActivity.getMainActivity());
            cassettes = cassetteCollector.update();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (cassettes == null ) {
                return;
            }

            db.clearCassette(); //delete table and Insert table from db

            for (int i = 0; i < cassettes.length; ++i)
                db.updateCassette(cassettes[i]);
        }
    }

    private class UpdateTriggerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            triggerListCollect = new TriggerListCollect(MainActivity.getMainActivity());
            triggerListCollect.update();
            return null;
        }

    }
}
