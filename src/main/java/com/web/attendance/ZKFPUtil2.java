package com.web.attendance;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.app.base.data.ApiResponseResult;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;

public class ZKFPUtil2 {
	
	//the width of fingerprint image
	int fpWidth = 0;
	//the height of fingerprint image
	int fpHeight = 0;
	//for verify test
	private byte[] lastRegTemp = new byte[2048];
	//the length of lastRegTemp
	private int cbRegTemp = 0;
	//pre-register template
	private byte[][] regtemparray = new byte[3][2048];
	//Register
	private boolean bRegister = false;
	//Identify
	private boolean bIdentify = true;
	//finger id
	private int iFid = 1;
	
	private int nFakeFunOn = 1;
	//must be 3
	static final int enroll_cnt = 3;
	//the index of pre-register function
	private int enroll_idx = 0;
	
	private byte[] imgbuf = null;
	private byte[] template = new byte[2048];
	private int[] templateLen = new int[1];
	
	
	private boolean mbStop = true;
	private long mhDevice = 0;
	private long mhDB = 0;
	private WorkThread workThread = null;
	
	private void FreeSensor()
	{
		mbStop = true;
		try {		//wait for thread stopping
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (0 != mhDB)
		{
			FingerprintSensorEx.DBFree(mhDB);
			mhDB = 0;
		}
		if (0 != mhDevice)
		{
			FingerprintSensorEx.CloseDevice(mhDevice);
			mhDevice = 0;
		}
		FingerprintSensorEx.Terminate();
	}
	
	public static void writeBitmap(byte[] imageBuf, int nWidth, int nHeight,
			String path) throws IOException {
		java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
		java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

		int w = (((nWidth+3)/4)*4);
		int bfType = 0x424d; // 浣嶅浘鏂囦欢绫诲瀷锛�0鈥�1瀛楄妭锛�
		int bfSize = 54 + 1024 + w * nHeight;// bmp鏂囦欢鐨勫ぇ灏忥紙2鈥�5瀛楄妭锛�
		int bfReserved1 = 0;// 浣嶅浘鏂囦欢淇濈暀瀛楋紝蹇呴』涓�0锛�6-7瀛楄妭锛�
		int bfReserved2 = 0;// 浣嶅浘鏂囦欢淇濈暀瀛楋紝蹇呴』涓�0锛�8-9瀛楄妭锛�
		int bfOffBits = 54 + 1024;// 鏂囦欢澶村紑濮嬪埌浣嶅浘瀹為檯鏁版嵁涔嬮棿鐨勫瓧鑺傜殑鍋忕Щ閲忥紙10-13瀛楄妭锛�

		dos.writeShort(bfType); // 杈撳叆浣嶅浘鏂囦欢绫诲瀷'BM'
		dos.write(changeByte(bfSize), 0, 4); // 杈撳叆浣嶅浘鏂囦欢澶у皬
		dos.write(changeByte(bfReserved1), 0, 2);// 杈撳叆浣嶅浘鏂囦欢淇濈暀瀛�
		dos.write(changeByte(bfReserved2), 0, 2);// 杈撳叆浣嶅浘鏂囦欢淇濈暀瀛�
		dos.write(changeByte(bfOffBits), 0, 4);// 杈撳叆浣嶅浘鏂囦欢鍋忕Щ閲�

		int biSize = 40;// 淇℃伅澶存墍闇�鐨勫瓧鑺傛暟锛�14-17瀛楄妭锛�
		int biWidth = nWidth;// 浣嶅浘鐨勫锛�18-21瀛楄妭锛�
		int biHeight = nHeight;// 浣嶅浘鐨勯珮锛�22-25瀛楄妭锛�
		int biPlanes = 1; // 鐩爣璁惧鐨勭骇鍒紝蹇呴』鏄�1锛�26-27瀛楄妭锛�
		int biBitcount = 8;// 姣忎釜鍍忕礌鎵�闇�鐨勪綅鏁帮紙28-29瀛楄妭锛夛紝蹇呴』鏄�1浣嶏紙鍙岃壊锛夈��4浣嶏紙16鑹诧級銆�8浣嶏紙256鑹诧級鎴栬��24浣嶏紙鐪熷僵鑹诧級涔嬩竴銆�
		int biCompression = 0;// 浣嶅浘鍘嬬缉绫诲瀷锛屽繀椤绘槸0锛堜笉鍘嬬缉锛夛紙30-33瀛楄妭锛夈��1锛圔I_RLEB鍘嬬缉绫诲瀷锛夋垨2锛圔I_RLE4鍘嬬缉绫诲瀷锛変箣涓�銆�
		int biSizeImage = w * nHeight;// 瀹為檯浣嶅浘鍥惧儚鐨勫ぇ灏忥紝鍗虫暣涓疄闄呯粯鍒剁殑鍥惧儚澶у皬锛�34-37瀛楄妭锛�
		int biXPelsPerMeter = 0;// 浣嶅浘姘村钩鍒嗚鲸鐜囷紝姣忕背鍍忕礌鏁帮紙38-41瀛楄妭锛夎繖涓暟鏄郴缁熼粯璁ゅ��
		int biYPelsPerMeter = 0;// 浣嶅浘鍨傜洿鍒嗚鲸鐜囷紝姣忕背鍍忕礌鏁帮紙42-45瀛楄妭锛夎繖涓暟鏄郴缁熼粯璁ゅ��
		int biClrUsed = 0;// 浣嶅浘瀹為檯浣跨敤鐨勯鑹茶〃涓殑棰滆壊鏁帮紙46-49瀛楄妭锛夛紝濡傛灉涓�0鐨勮瘽锛岃鏄庡叏閮ㄤ娇鐢ㄤ簡
		int biClrImportant = 0;// 浣嶅浘鏄剧ず杩囩▼涓噸瑕佺殑棰滆壊鏁�(50-53瀛楄妭)锛屽鏋滀负0鐨勮瘽锛岃鏄庡叏閮ㄩ噸瑕�

		dos.write(changeByte(biSize), 0, 4);// 杈撳叆淇℃伅澶存暟鎹殑鎬诲瓧鑺傛暟
		dos.write(changeByte(biWidth), 0, 4);// 杈撳叆浣嶅浘鐨勫
		dos.write(changeByte(biHeight), 0, 4);// 杈撳叆浣嶅浘鐨勯珮
		dos.write(changeByte(biPlanes), 0, 2);// 杈撳叆浣嶅浘鐨勭洰鏍囪澶囩骇鍒�
		dos.write(changeByte(biBitcount), 0, 2);// 杈撳叆姣忎釜鍍忕礌鍗犳嵁鐨勫瓧鑺傛暟
		dos.write(changeByte(biCompression), 0, 4);// 杈撳叆浣嶅浘鐨勫帇缂╃被鍨�
		dos.write(changeByte(biSizeImage), 0, 4);// 杈撳叆浣嶅浘鐨勫疄闄呭ぇ灏�
		dos.write(changeByte(biXPelsPerMeter), 0, 4);// 杈撳叆浣嶅浘鐨勬按骞冲垎杈ㄧ巼
		dos.write(changeByte(biYPelsPerMeter), 0, 4);// 杈撳叆浣嶅浘鐨勫瀭鐩村垎杈ㄧ巼
		dos.write(changeByte(biClrUsed), 0, 4);// 杈撳叆浣嶅浘浣跨敤鐨勬�婚鑹叉暟
		dos.write(changeByte(biClrImportant), 0, 4);// 杈撳叆浣嶅浘浣跨敤杩囩▼涓噸瑕佺殑棰滆壊鏁�

		for (int i = 0; i < 256; i++) {
			dos.writeByte(i);
			dos.writeByte(i);
			dos.writeByte(i);
			dos.writeByte(0);
		}

		byte[] filter = null;
		if (w > nWidth)
		{
			filter = new byte[w-nWidth];
		}
		
		for(int i=0;i<nHeight;i++)
		{
			dos.write(imageBuf, (nHeight-1-i)*nWidth, nWidth);
			if (w > nWidth)
				dos.write(filter, 0, w-nWidth);
		}
		dos.flush();
		dos.close();
		fos.close();
	}

	public static byte[] changeByte(int data) {
		return intToByteArray(data);
	}
	
	public static byte[] intToByteArray (final int number) {
		byte[] abyte = new byte[4];  
	    // "&" 涓庯紙AND锛夛紝瀵逛袱涓暣鍨嬫搷浣滄暟涓搴斾綅鎵ц甯冨皵浠ｆ暟锛屼袱涓綅閮戒负1鏃惰緭鍑�1锛屽惁鍒�0銆�  
	    abyte[0] = (byte) (0xff & number);  
	    // ">>"鍙崇Щ浣嶏紝鑻ヤ负姝ｆ暟鍒欓珮浣嶈ˉ0锛岃嫢涓鸿礋鏁板垯楂樹綅琛�1  
	    abyte[1] = (byte) ((0xff00 & number) >> 8);  
	    abyte[2] = (byte) ((0xff0000 & number) >> 16);  
	    abyte[3] = (byte) ((0xff000000 & number) >> 24);  
	    return abyte; 
	}	 
		 
		public static int byteArrayToInt(byte[] bytes) {
			int number = bytes[0] & 0xFF;  
		    // "|="鎸変綅鎴栬祴鍊笺��  
		    number |= ((bytes[1] << 8) & 0xFF00);  
		    number |= ((bytes[2] << 16) & 0xFF0000);  
		    number |= ((bytes[3] << 24) & 0xFF000000);  
		    return number;  
		 }
	
		public class WorkThread extends Thread {
	        @Override
	        public void run() {
	            super.run();
	            int ret = 0;
	            while (!mbStop) {
	            	templateLen[0] = 2048;
	            	if (0 == (ret = FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLen)))
	            	{
	            		if (nFakeFunOn == 1)
                    	{
                    		byte[] paramValue = new byte[4];
            				int[] size = new int[1];
            				size[0] = 4;
            				int nFakeStatus = 0;
            				//GetFakeStatus
            				ret = FingerprintSensorEx.GetParameters(mhDevice, 2004, paramValue, size);
            				nFakeStatus = byteArrayToInt(paramValue);
            				System.out.println("ret = "+ ret +",nFakeStatus=" + nFakeStatus);
            				if (0 == ret && (byte)(nFakeStatus & 31) != 31)
            				{
            					//textArea.setText("Is a fake-finer?");
            					System.out.println("请问是正常的人体指纹吗?");
            					return;
            				}

                    	}
                    	OnCatpureOK(imgbuf);
                    	OnExtractOK(template, templateLen[0]);
                    	String strBase64 = FingerprintSensorEx.BlobToBase64(template, templateLen[0]);
                    	System.out.println("strBase64test=" + strBase64);
	            	}
	                try {
	                    Thread.sleep(500);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }

	            }
	        }

			private void runOnUiThread(Runnable runnable) {
				// TODO Auto-generated method stub
				
			}
	    }
		
		private void OnCatpureOK(byte[] imgBuf)
		{
			try {
				writeBitmap(imgBuf, fpWidth, fpHeight, "c://fingerprint"+enroll_idx+".png");
				//btnImg.setIcon(new ImageIcon(ImageIO.read(new File("fingerprint.bmp"))));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void OnExtractOK(byte[] template, int len)
		{
			if(bRegister)
			{
				int[] fid = new int[1];
				int[] score = new int [1];
                int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
                if (ret == 0)
                {
                    //textArea.setText("the finger already enroll by " + fid[0] + ",cancel enroll");
                    System.out.println("the finger already enroll by " + fid[0] + ",cancel enroll");
                	bRegister = false;
                    enroll_idx = 0;
                    return;
                }
                if (enroll_idx > 0 && FingerprintSensorEx.DBMatch(mhDB, regtemparray[enroll_idx-1], template) <= 0)
                {
                	//textArea.setText("please press the same finger 3 times for the enrollment");
                	System.out.println("please press the same finger 3 times for the enrollment");
                	return;
                }
                System.arraycopy(template, 0, regtemparray[enroll_idx], 0, 2048);
                enroll_idx++;
                if (enroll_idx == 3) {
                	int[] _retLen = new int[1];
                    _retLen[0] = 2048;
                    byte[] regTemp = new byte[_retLen[0]];
                    
                    if (0 == (ret = FingerprintSensorEx.DBMerge(mhDB, regtemparray[0], regtemparray[1], regtemparray[2], regTemp, _retLen)) &&
                    		0 == (ret = FingerprintSensorEx.DBAdd(mhDB, iFid, regTemp))) {
                    	iFid++;
                    	cbRegTemp = _retLen[0];
                        System.arraycopy(regTemp, 0, lastRegTemp, 0, cbRegTemp);
                        String strBase64 = FingerprintSensorEx.BlobToBase64(regTemp, cbRegTemp);
                        //Base64 Template
                        System.out.println("最终的:"+strBase64);
                        //textArea.setText("enroll succ");
                        System.out.println("nroll succ");
                    } else {
                    	//textArea.setText("enroll fail, error code=" + ret);
                    	System.out.println("enroll fail, error code=" + ret);
                    }
                    bRegister = false;
                } else {
                	System.out.println("You need to press the " + (3 - enroll_idx) + " times fingerprint");
                	//textArea.setText("You need to press the " + (3 - enroll_idx) + " times fingerprint");
                }
			}
			else
			{
				if (bIdentify)
				{
					int[] fid = new int[1];
					int[] score = new int [1];
					int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
                    if (ret == 0)
                    {
                    	System.out.println("Identify succ, fid=" + fid[0] + ",score=" + score[0]);
                    	//textArea.setText("Identify succ, fid=" + fid[0] + ",score=" + score[0]);
                    }
                    else
                    {
                    	System.out.println("Identify fail, errcode=" + ret);
                    	//textArea.setText("Identify fail, errcode=" + ret);
                    }
                        
				}
				else
				{
					if(cbRegTemp <= 0)
					{
						System.out.println("请先注册!");
						//textArea.setText("Please register first!");
					}
					else
					{
						int ret = FingerprintSensorEx.DBMatch(mhDB, lastRegTemp, template);
						if(ret > 0)
						{
							System.out.println("Verify succ, score=" + ret);
							//textArea.setText("Verify succ, score=" + ret);
						}
						else
						{
							System.out.println("Verify fail, ret=" + ret);
							//textArea.setText("Verify fail, ret=" + ret);
						}
					}
				}
			}
		}
		
		//打开
		public ApiResponseResult open() {
			// TODO Auto-generated method stub
			if (0 != mhDevice)
			{
				//already inited
				//textArea.setText("Please close device first!");
				System.out.println("Please close device first!");
				return ApiResponseResult.failure("请先关闭指纹仪！");
			}
			int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;
			//Initialize
			cbRegTemp = 0;
			bRegister = false;
			bIdentify = false;
			iFid = 1;
			enroll_idx = 0;
			if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init())
			{
				//textArea.setText("Init failed!");
				System.out.println("Init failed!");
				return ApiResponseResult.failure("初始化失败！");
			}
			ret = FingerprintSensorEx.GetDeviceCount();
			if (ret < 0)
			{
				//textArea.setText("No devices connected!");
				System.out.println("No devices connected!");
				FreeSensor();
				return ApiResponseResult.failure("未连接任何设备！");
			}
			if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0)))
			{
				//textArea.setText("Open device fail, ret = " + ret + "!");
				System.out.println("Open device fail, ret = " + ret + "!");
				FreeSensor();
				return ApiResponseResult.failure("打开设备失败,et = " + ret + "！");
			}
			if (0 == (mhDB = FingerprintSensorEx.DBInit()))
			{
				//textArea.setText("Init DB fail, ret = " + ret + "!");
				System.out.println("Init DB fail, ret = " + ret + "!");
				FreeSensor();
				return ApiResponseResult.failure("初始化数据库失败,et = " + ret + "！");
			}
			
			
			//set fakefun off
			
			//FingerprintSensorEx.SetParameter(mhDevice, 2002, changeByte(nFakeFunOn), 4);
						
			byte[] paramValue = new byte[4];
			int[] size = new int[1];
			//GetFakeOn
			//size[0] = 4;
			//FingerprintSensorEx.GetParameters(mhDevice, 2002, paramValue, size);
			//nFakeFunOn = byteArrayToInt(paramValue);
			
			size[0] = 4;
			FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);
			fpWidth = byteArrayToInt(paramValue);
			size[0] = 4;
			FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);
			fpHeight = byteArrayToInt(paramValue);
			//width = fingerprintSensor.getImageWidth();
			//height = fingerprintSensor.getImageHeight();
			imgbuf = new byte[fpWidth*fpHeight];
			//btnImg.resize(fpWidth, fpHeight);
			mbStop = false;
			workThread = new WorkThread();
		    workThread.start();// 绾跨▼鍚姩
            //textArea.setText("Open succ!");
		    System.out.println("Open succ!");
		    
		    return ApiResponseResult.success("初始化成功！");
		}
		
		//注册指纹
		public ApiResponseResult enroll(){
			if(0 == mhDevice)
			{
				//textArea.setText("Please Open device first!");
				return ApiResponseResult.failure("请先打开设备!");
			}
			if(!bRegister)
			{
				enroll_idx = 0;
				bRegister = true;
				//textArea.setText("Please your finger 3 times!");
				return ApiResponseResult.success("请摁压您的手指3次!");
			}
			return ApiResponseResult.failure("注册失败");
		}
		public ApiResponseResult close() {
			// TODO Auto-generated method stub
			FreeSensor();
			System.out.println("关闭成功!");
			return ApiResponseResult.success("关闭成功!");
		}
		
		public static void main(String[] args) {
			
		}
}
