package com.redread.bookrack;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeProvider;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.redread.MyApplication;
import com.redread.R;
import com.redread.databinding.LayoutMainBinding;
import com.redread.model.bean.Book;
import com.redread.model.entity.DownLoad;
import com.redread.model.gen.DownLoadDao;
import com.redread.utils.Constant;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

/**
 * pdf类型的阅读
 */
public class Activity_pdfReader extends AppCompatActivity implements OnLoadCompleteListener {
public static String extra_book="extra_pdf_book";
public static String extra_bookId="extra_bookId";
    private LayoutMainBinding binding;

    private long time;

    private static Book book;
    private DownLoadDao dao;

    public static void loadPdfFile(Context mContext,long id,Book book){
        Intent intent = new Intent();
        intent.putExtra(extra_bookId, id);
        intent.putExtra(extra_bookId,book);
        Activity_pdfReader.book=book;
        intent.setClass(mContext, Activity_pdfReader.class);
        mContext.startActivity(intent);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_main);
        Bundle bundle=getIntent().getExtras();
        //处理一些需要的参数
//        book = (Book) this.getIntent().getSerializableExtra(extra_book);
//        long id=this.getIntent().getLongExtra(extra_bookId,-1);

        time = System.currentTimeMillis();

        dao = MyApplication.getInstances().getDaoSession().getDownLoadDao();

//        binding.pdfView.fromAsset("tanke.pdf")
        File bookDir = new File(book.getBookDir());
        if (!bookDir.exists()) {
            Toast.makeText(this, "书不见了，重新下载吧！", Toast.LENGTH_LONG).show();
            //将书的状态改为失败
            DownLoad task = dao.load(book.getId());
            task.setStatus(Constant.DOWN_STATUS_FAILE);
            dao.update(task);
            finish();
            return;
        }
        binding.pdfView.fromFile(bookDir)
//        .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0).onLoad(this)

                // allows to draw something on the current page, usually visible in the middle of the screen
                /**.onDraw(new OnDrawListener() {
                @Override public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                }
                });
                 // allows to draw something on all pages, separately for every page. Called only for visible pages
                 //        .onDrawAll(onDrawListener)
                 .onLoad(new OnLoadCompleteListener() {
                @Override public void loadComplete(int nbPages) {

                }
                }) // called after document is loaded and starts to be rendered
                 .onPageChange(new OnPageChangeListener() {
                @Override public void onPageChanged(int page, int pageCount) {

                }
                })

                 .onError(new OnErrorListener() {
                @Override public void onError(Throwable t) {

                }
                })
                 .onPageError(new OnPageErrorListener() {
                @Override public void onPageError(int page, Throwable t) {

                }
                })
                 .onRender(new OnRenderListener() {
                @Override public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {

                }
                }) // called after document is rendered for the first time
                 // called on single tap, return true if handled, false to toggle scroll handle visibility
                 .onTap(new OnTapListener() {
                @Override public boolean onTap(MotionEvent e) {
                return false;
                }
                })
                 //        .onLongPress(onLongPressListener)
                 .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                 .password(null)
                 .scrollHandle(null)
                 .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                 // spacing between pages in dp. To define spacing color, set view background
                 .spacing(0)
                 //        .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                 //        .linkHandler(DefaultLinkHandler)
                 //        .pageFitPolicy(FitPolicy.WIDTH)**/
                .onPageScroll(new OnPageScrollListener() {
                    @Override public void onPageScrolled(int page, float positionOffset) {
                        //在此处记录当前页
                        DownLoad task=dao.load(book.getId());
                        task.setCurrentPage(page);
                        //记算进度
                        float p = page / (float)task.getTotalPage();
                        task.setReadProgress((int)(p*100));
                        dao.update(task);
                        binding.progress.setText(page+"/"+task.getTotalPage());
                    }
                })
                .pageSnap(true) // snap pages to screen boundaries
                .pageFling(true) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night mode
                .load();

    }

    private String TAG = getClass().getName();

    @Override
    public void loadComplete(int nbPages) {
        //更新总页数
        DownLoad task=dao.load(book.getId());
        task.setTotalPage(nbPages);
        dao.update(task);

        String payTime = (System.currentTimeMillis() - time) + "";
        Log.e(TAG, payTime + "     ========================loadComplete: ========================  跳转到上次阅读的地方,总页数：" + nbPages);
        if (task.getCurrentPage() > 0) {
//            binding.pdfView.jumpTo(task.getCurrentPage());
            binding.progress.setText(task.getCurrentPage()+"/"+task.getTotalPage());
            mHandler.sendEmptyMessageDelayed(0,1000);
        }

//        PdfDocument.Meta meta=binding.pdfView.getDocumentMeta();
//        List<PdfDocument.Bookmark> bookmarks=binding.pdfView.getTableOfContents();
//       AccessibilityNodeProvider provider= binding.pdfView.getAccessibilityNodeProvider();
    }


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            binding.pdfView.jumpTo(book.getCurrentPage());
        }
    };
}
