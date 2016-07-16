package ubicomp.rehabdiary.utility.dialog;


import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ubicomp.rehabdiary.R;
import ubicomp.rehabdiary.main_activity.KetdiaryApplication;
import ubicomp.rehabdiary.utility.system.PreferenceControl;

/**
 * Created by yu on 16/6/13.
 */
public class PasswordPage {
	private RelativeLayout mainLayout;
	private Context context;
	private LayoutInflater inflater;
	private RelativeLayout boxLayout = null;
	private TextView titleText;
	private ImageView oneImage, twoImage, threeImage, fourImage, fiveImage,
			sixImage, sevenImage, eightImage, nineImage, backImage,
			clearImage, zeroImage, cancelImage;
	private ImageView[] starImage = new ImageView[4];
	private int[] password = new int[4];
	private int enteredNum;
	private int[] temp_password = new int[4];
	private int[] new_password = new int[4];
	private int[] new_again_password = new int[4];
	private int[][] ori_position = new int[4][4];

	private boolean clickAble;
	private FailTimer failTimer;

	private int type, state;

	public static final int LOGIN_APP 			= 0;
	public static final int INIT_PASSWORD 		= 1;
	public static final int SET_PASSWORD 		= 2;

	public static final int LOGIN_STATE 		= 0;
	public static final int SET_FIRST_STATE 	= 1;
	public static final int SET_SECOND_STATE 	= 2;
	public static final int ERROR_STATE 		= 3;
	public static final int SECOND_ERROR_STATE 	= 4;

	public PasswordPage(RelativeLayout mainLayout, int type){
		this.context = KetdiaryApplication.getContext();
		this.inflater = LayoutInflater.from(context);
		this.mainLayout = mainLayout;
		this.type = type;
	}

	public void initialize(){
		if(type == LOGIN_APP)
			state = LOGIN_STATE;
		if(type == INIT_PASSWORD)
			state = SET_FIRST_STATE;
		if(type == SET_PASSWORD)
			state = LOGIN_STATE;

		enteredNum = 0;
		clickAble = true;
		getUserPassword();

		boxLayout = (RelativeLayout) inflater.inflate(
				R.layout.activity_password, null);
		boxLayout.setVisibility(View.INVISIBLE);

		titleText = (TextView)boxLayout.findViewById(R.id.pswLock_title);

		oneImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_1);
		twoImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_2);
		threeImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_3);
		fourImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_4);
		fiveImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_5);
		sixImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_6);
		sevenImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_7);
		eightImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_8);
		nineImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_9);
		zeroImage = (ImageView)boxLayout.findViewById(R.id.pswLock_num_0);

		backImage = (ImageView)boxLayout.findViewById(R.id.pswLock_delete);
		clearImage = (ImageView)boxLayout.findViewById(R.id.pswLock_cancel);
		cancelImage = (ImageView)boxLayout.findViewById(R.id.pswLock_back);

		starImage[0] = (ImageView)boxLayout.findViewById(R.id.pswLock_star1);
		starImage[1] = (ImageView)boxLayout.findViewById(R.id.pswLock_star2);
		starImage[2] = (ImageView)boxLayout.findViewById(R.id.pswLock_star3);
		starImage[3] = (ImageView)boxLayout.findViewById(R.id.pswLock_star4);

		oneImage.setOnClickListener(new ButtonOnClickListener());
		twoImage.setOnClickListener(new ButtonOnClickListener());
		threeImage.setOnClickListener(new ButtonOnClickListener());
		fourImage.setOnClickListener(new ButtonOnClickListener());
		fiveImage.setOnClickListener(new ButtonOnClickListener());
		sixImage.setOnClickListener(new ButtonOnClickListener());
		sevenImage.setOnClickListener(new ButtonOnClickListener());
		eightImage.setOnClickListener(new ButtonOnClickListener());
		nineImage.setOnClickListener(new ButtonOnClickListener());
		zeroImage.setOnClickListener(new ButtonOnClickListener());
		backImage.setOnClickListener(new ButtonOnClickListener());
		clearImage.setOnClickListener(new ButtonOnClickListener());
		cancelImage.setOnClickListener(new ButtonOnClickListener());

		for(int i = 0; i < 4; i++)
		{
			ori_position[i][0] = starImage[i].getLeft();
			ori_position[i][1] = starImage[i].getTop();
			ori_position[i][2] = starImage[i].getRight();
			ori_position[i][3] = starImage[i].getBottom();

		}

		mainLayout.addView(boxLayout);

	}

	/** remove the dialog and release the resources */
	public void clear() {
		if (mainLayout != null && boxLayout != null
				&& boxLayout.getParent() != null
				&& boxLayout.getParent().equals(mainLayout))
			mainLayout.removeView(boxLayout);
	}

	/** close the dialog */
	public void close() {
		if (boxLayout != null)
			boxLayout.setVisibility(View.INVISIBLE);
	}

	/** show the dialog */
	public void show() {
		updateView();
		boxLayout.setVisibility(View.VISIBLE);

		// lock back button
		if(type == LOGIN_APP)
		{
			cancelImage.setVisibility(View.INVISIBLE);
		}
		else
		{
			cancelImage.setVisibility(View.VISIBLE);
		}
	}

	private boolean checkPassword(){
		if(state == LOGIN_STATE || state == ERROR_STATE){
			for(int i = 0; i < 4; i++)
				if(password[i] != temp_password[i])
					return false;
			return true;
		}

		if(state == SET_FIRST_STATE || state == SECOND_ERROR_STATE){
			for(int i = 0; i < 4; i++)
				new_password[i] = password[i];
			return true;
		}

		if(state == SET_SECOND_STATE){
			for(int i = 0; i < 4; i++)
				if(password[i] != new_password[i])
					return false;
			return true;
		}
		return false;
	}

	private void updateView(){

		for(int i = 0; i < enteredNum; i++)
		{
			starImage[i].setVisibility(View.VISIBLE);
		}
		for(int i = enteredNum; i < 4; i++)
		{
			starImage[i].setVisibility(View.INVISIBLE);
		}

		switch (state)
		{
			case LOGIN_APP:
				titleText.setText("請輸入密碼");
				break;

			case ERROR_STATE:
				titleText.setText("密碼錯誤 再輸入一次");
				break;

			case SET_FIRST_STATE:
				titleText.setText("請輸入新密碼");
				break;

			case SET_SECOND_STATE:
				titleText.setText("再輸入一次新密碼");
				break;

			case SECOND_ERROR_STATE:
				titleText.setText("密碼不一致 請重新設定");
				break;
		}
	}

	private class ButtonOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if(!clickAble)
				return;
			switch (v.getId()){
				case R.id.pswLock_num_1:
					if(enteredNum < 4) {
						password[enteredNum] = 1;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_2:
					if(enteredNum < 4) {
						password[enteredNum] = 2;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_3:
					if(enteredNum < 4) {
						password[enteredNum] = 3;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_4:
					if(enteredNum < 4) {
						password[enteredNum] = 4;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_5:
					if(enteredNum < 4) {
						password[enteredNum] = 5;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_6:
					if(enteredNum < 4) {
						password[enteredNum] = 6;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_7:
					if(enteredNum < 4) {
						password[enteredNum] = 7;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_8:
					if(enteredNum < 4) {
						password[enteredNum] = 8;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_9:
					if(enteredNum < 4) {
						password[enteredNum] = 9;
						enteredNum++;
					}
					break;

				case R.id.pswLock_num_0:
					if(enteredNum < 4) {
						password[enteredNum] = 0;
						enteredNum++;
					}
					break;

				case R.id.pswLock_cancel:
					enteredNum = 0;
					break;

				case R.id.pswLock_delete:
					if(enteredNum > 0)
						enteredNum--;
					break;

				case R.id.pswLock_back:
					enteredNum = 0;
					close();
					break;


			}

			if(enteredNum == 4)
			{
				clickAble = false;
				boolean right = checkPassword();

				checkSate(right);

			}

			updateView();

		}
	}

	private void getUserPassword(){
		int n = PreferenceControl.getAppPassword();
		//int n = 7122;
		for(int i = 3; i >= 0; i--)
		{
			temp_password[i] = n % 10;
			n /= 10;
		}
	}

	private class FailTimer extends CountDownTimer {
		public FailTimer() {
			super(800, 40);
		}
		int[] gaps = {40, -80, 75 -70, 65,
				-60, 55, -50, 45, -40,
				35, -30, 25, -20, 15,
				-10, 10, -10, 10, -5,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int index = 0;
		@Override
		public void onFinish() {
            /*for(int i = 0; i < 4; i++)
            {
                starImage[i].layout(ori_position[i][0],
                        ori_position[i][1],
                        ori_position[i][2],
                        ori_position[i][3]);
            }*/

			enteredNum = 0;
			updateView();
			clickAble = true;

		}
		@Override
		public void onTick(long millisUntilFinished) {
			for(int i = 0; i < 4; i++)
			{
				starImage[i].layout(starImage[i].getLeft() + gaps[index],
						starImage[i].getTop(),
						starImage[i].getRight() + gaps[index],
						starImage[i].getBottom());
			}
			index++;
		}
	}

	public void checkSate(boolean right_password)
	{
		if(type == LOGIN_APP)
			switch(state)
			{
				case LOGIN_STATE:
					if(right_password)
					{
						close();
					}
					else
					{
						state = ERROR_STATE;
						failTimer = new FailTimer();
						failTimer.start();
					}
					break;

				case ERROR_STATE:
					if(right_password) {
						close();
					}
					else
					{
						failTimer = new FailTimer();
						failTimer.start();
					}
					break;
			}

		if(type == INIT_PASSWORD)
			switch(state)
			{
				case SECOND_ERROR_STATE:
				case SET_FIRST_STATE :
					if(right_password)
					{
						enteredNum = 0;
						state = SET_SECOND_STATE;
					}
					break;

				case SET_SECOND_STATE:
					if(right_password)
					{
						AbleAndSetPassword();
						close();
					}
					else
					{
						state = SECOND_ERROR_STATE;
						failTimer = new FailTimer();
						failTimer.start();
					}
					break;
			}

		if(type == SET_PASSWORD)
			switch(state)
			{
				case LOGIN_STATE:
					if(right_password)
					{
						enteredNum = 0;
						state = SET_FIRST_STATE;
					}
					else
					{
						failTimer = new FailTimer();
						failTimer.start();
						state = ERROR_STATE;
					}
					break;

				case ERROR_STATE:
					if(right_password)
						state = SET_FIRST_STATE;
					else
					{
						failTimer = new FailTimer();
						failTimer.start();
					}
					break;

				case SECOND_ERROR_STATE:
				case SET_FIRST_STATE :
					if(right_password)
					{
						enteredNum = 0;
						state = SET_SECOND_STATE;
					}
					break;

				case SET_SECOND_STATE:
					if(right_password)
					{
						AbleAndSetPassword();
						close();
					}
					else
					{
						state = SECOND_ERROR_STATE;
						failTimer = new FailTimer();
						failTimer.start();
					}
					break;
			}
		if(right_password)
			clickAble = true;
	}

	public void AbleAndSetPassword(){
		int password_value = 0;
		for(int i = 0, k = 1000; i < 4; i++, k /= 10)
			password_value += new_password[i] * k;

		PreferenceControl.setAppPasswordAble(1);
		PreferenceControl.setAppPassword(password_value);
	}
}
