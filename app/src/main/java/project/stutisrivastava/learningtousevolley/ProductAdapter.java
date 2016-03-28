package project.stutisrivastava.learningtousevolley;

/**
 * Created by stutisrivastava on 2/29/16.
 */
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

import project.stutisrivastava.learningtousevolley.pojo.Product;
import project.stutisrivastava.learningtousevolley.util.LearningToUseVolley;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    List<Product> products;
    private LearningToUseVolley helper = LearningToUseVolley.getInstance();

    public ProductAdapter(List<Product> products){
        this.products = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_product_card_view, viewGroup, false);
        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;

    }

    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int i) {
        final int pos=i;
        Product product = products.get(i);
        productViewHolder.productName.setText(product.getProductName());
        loadImg(product.getProductImgUrl(),productViewHolder.productPhoto);
    }

    private void loadImg(final String imageUrl, final ImageView mImageView) {
        // Retrieves an image specified by the URL, and displays it in the UI
        Log.e("TAG","loadImg");
        ImageRequest request = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG","loadImg "+error.toString()+" for "+imageUrl);
                        return;
                    }
                });
        helper.add(request);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Product getItem(int pos){
        return products.get(pos);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView productName;
        ImageView productPhoto;

        ProductViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cvProduct);
            productName = (TextView) itemView.findViewById(R.id.tv_card_product_name);
            productPhoto = (ImageView) itemView.findViewById(R.id.im_card_product_photo);
        }
    }
}



