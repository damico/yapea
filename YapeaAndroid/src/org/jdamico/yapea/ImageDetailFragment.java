package org.jdamico.yapea;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdamico.yapea.commons.Utils;
import org.jdamico.yapea.commons.YapeaException;
import org.jdamico.yapea.crypto.CryptoUtils;
import org.jdamico.yapea.dataobjects.ImageItemObj;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A fragment representing a single Image detail screen. This fragment is either
 * contained in a {@link ImageListActivity} in two-pane mode (on tablets) or a
 * {@link ImageDetailActivity} on handsets.
 */
public class ImageDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private ImageItemObj mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ImageDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Map<String, ImageItemObj> ITEM_MAP = new HashMap<String, ImageItemObj>();
		
		String yapeaDir = Utils.getInstance().getYapeaImageDir();
		
		File imageDir = new File(yapeaDir);
		
		if(imageDir.exists()){
			
			String[] contents = imageDir.list();
			for (int i = 0; i < contents.length; i++) {
				ITEM_MAP.put(String.valueOf(i+1), new ImageItemObj(contents[i], String.valueOf(i+1)));
			}
			
		} //TODO add exception

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			//((TextView) rootView.findViewById(R.id.image_detail)).setText(mItem.getImageName());
			
			
			String yapeaDir = Utils.getInstance().getYapeaImageDir();
			String file = yapeaDir+mItem.getImageName();
			
			
			
			File newfile = new File(file);
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}       


			try {
				byte[] cipherContent = Utils.getInstance().getBytesFromFile(newfile);
				byte[] plainContent = CryptoUtils.getInstance().dec(rootView.getContext(), CryptoUtils.getInstance().retrieveKeyFromCache(rootView.getContext()), cipherContent, Utils.getInstance().getConfigFile(rootView.getContext()).getEncAlgo());
				
				Bitmap bitmap = Utils.getInstance().byteArrayToBitmap(plainContent);
				if(bitmap.getHeight() > 2048 || bitmap.getWidth() > 2048){
					
					int h = 0;
					int w = 0;
					int p = 0;
					if(bitmap.getHeight() >= bitmap.getWidth()){
						p = (bitmap.getWidth() * 100) / bitmap.getHeight();
						h = 2048;
						w = (h*p)/100;
					}else{
						p = (bitmap.getHeight() * 100) / bitmap.getWidth();
						w = 2048;
						h = (w*p)/100;
					}
					
					bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
				}
				
				((ImageView) rootView.findViewById(R.id.image_detail)).setImageBitmap(bitmap);
			} catch (YapeaException e) {
				e.printStackTrace();
			}
			
			
			
			
			//((ImageView) rootView.findViewById(R.id.image_detail)).setImageURI(outputFileUri);
			
		}

		return rootView;
	}
}
