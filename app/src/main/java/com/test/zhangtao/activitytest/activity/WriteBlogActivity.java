package com.test.zhangtao.activitytest.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sqk.emojirelease.FaceFragment;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.adapter.ChoosePhotoListAdapter;
import com.test.zhangtao.activitytest.contacts.NetUrlContacts;
import com.test.zhangtao.activitytest.entity.StatusEntity;
import com.test.zhangtao.activitytest.http.BlogHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.util.MyActionSheet;
import com.test.zhangtao.activitytest.widget.DBToolBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.HorizontalListView;

/**
 * Created by zhangtao on 16/11/14.
 */
public class WriteBlogActivity extends BaseWriteActivity
{
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;

    @ViewInject(R.id.blog_creating_toolBar)
    private DBToolBar mToolBar;
    @ViewInject(R.id.lv_photo)
    private HorizontalListView mLvPhoto;
    @ViewInject(R.id.blog_create_status)
    private EditText mCreateContent;

    private List<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;
    private FunctionConfig functionConfig;
    private BlogHttpHelper blogHttpHelper = BlogHttpHelper.getInstance();
    private Bitmap statusBitmap;
    private FaceFragment faceFragment = FaceFragment.Instance();
    private boolean isShow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.write_blog_activity);
        setTheme(R.style.ActionSheetStyleiOS7);
        ViewUtils.inject(this);

        getSupportFragmentManager().beginTransaction().add(R.id.Container , faceFragment).hide(faceFragment).commit();

        initToolBar();
        initData();
    }

    private void initToolBar()
    {
        mToolBar.setLeftButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createStatus();
            }
        });
    }

    @OnClick(R.id.add_photo)
    public void addPhoto(View view)
    {
        functionConfig = MyApplication.getInstance().getFunctionConfig();
        if (functionConfig == null)
        {
            return;
        }
        initActionSheet();
    }

    @OnClick(R.id.at_someOne)
    public void atSomeOne(View view)
    {
        Intent intent = new Intent(this , FriendListActivity.class);
        startActivityForResult(intent , NetUrlContacts.REQUEST_STATUS_CODE);
    }

    @OnClick(R.id.show_face_selector)
    public void showFaceSelector(View view)
    {
        if (isShow)
        {
            getSupportFragmentManager().beginTransaction().hide(faceFragment).commit();
            isShow = false;
        }
        else
        {
            getSupportFragmentManager().beginTransaction().show(faceFragment).commit();
            isShow = true;
        }
    }

    private void createStatus()
    {
        if (TextUtils.isEmpty(mCreateContent.getText().toString().trim()))
        {
            Toast.makeText(WriteBlogActivity.this , "请填写评论！！！" , Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String , Object> map = new HashMap<>(3);
        map.put("status" , mCreateContent.getText().toString());
        String url = "";
        if (statusBitmap != null)
        {
            url = url + NetUrlContacts.STATUS_WITH_PIC;
            map.put("pic" , statusBitmap);
        }
        else
        {
            url = url + NetUrlContacts.STATUS_CREATE_URL;
        }
        Log.d("Url" , url + "");

        blogHttpHelper.request(url , NetUrlContacts.METHOD_POST,
                map, new SimpleCallBack<StatusEntity>(this)
                {
                    @Override
                    public void onSuccess(String response, StatusEntity statusEntity)
                    {
                        Log.d("Bitmap" , "not null");
                        Toast.makeText(WriteBlogActivity.this , "Status Success" , Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(String error, int errorCode)
                    {
                    }
                });
    }

    private void initData()
    {
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this , mPhotoList);
        mLvPhoto.setAdapter(mChoosePhotoListAdapter);

        initImageLoader(this);
        initFresco();
    }

    private void initActionSheet()
    {
        new MyActionSheet(WriteBlogActivity.this , getSupportFragmentManager() , NetUrlContacts.BUTTON_TITLES)
        {
            @Override
            protected void actionDetail(int index)
            {
                String path = "/sdcard/pk1-2.jpg";
                switch (index)
                {
                    case 0:
                        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY , functionConfig , mOnHandlerResultCallBack);
                        break;
                    case 1:
                        GalleryFinal.openCamera(REQUEST_CODE_CAMERA , functionConfig , mOnHandlerResultCallBack);
                        break;
                    case 2:
                        if (new File(path).exists())
                        {
                            GalleryFinal.openCrop(REQUEST_CODE_CROP , functionConfig , path , mOnHandlerResultCallBack);
                        }
                        else
                        {
                            Toast.makeText(WriteBlogActivity.this , "图片不存在" , Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        if (new File(path).exists())
                        {
                            GalleryFinal.openEdit(REQUEST_CODE_EDIT , functionConfig , path , mOnHandlerResultCallBack);
                        }
                        else
                        {
                            Toast.makeText(WriteBlogActivity.this , "图片不存在" , Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(40 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

    private void initFresco()
    {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .build();
        Fresco.initialize(this , config);
    }


    private GalleryFinal.OnHanlderResultCallback mOnHandlerResultCallBack = new GalleryFinal.OnHanlderResultCallback()
    {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList)
        {
            if (resultList != null)
            {
                mPhotoList.addAll(resultList);
                mChoosePhotoListAdapter.notifyDataSetChanged();
                statusBitmap = BitmapFactory.decodeFile(mPhotoList.get(0).getPhotoPath());
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg)
        {
            Toast.makeText(WriteBlogActivity.this , errorMsg , Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public EditText setEditContent()
    {
        return mCreateContent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data != null && requestCode == NetUrlContacts.REQUEST_STATUS_CODE)
        {
            String resultData = data.getStringExtra(NetUrlContacts.FRIEND_NAME);
            String beforeData = mCreateContent.getText().toString();
            mCreateContent.setText(beforeData + resultData);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (statusBitmap != null && !statusBitmap.isRecycled())
        {
            statusBitmap.recycle();
        }
    }
}
