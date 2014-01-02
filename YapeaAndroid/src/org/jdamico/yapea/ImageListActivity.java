package org.jdamico.yapea;

/*
 * This file is part of YAPEA.
 * 
 *    YAPEA is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License (version 2) 
 *    as published by the Free Software Foundation.
 *
 *    YAPEA is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with YAPEA.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * An activity representing a list of Images. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ImageDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ImageListFragment} and the item details (if present) is a
 * {@link ImageDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ImageListFragment.Callbacks} interface to listen for item selections.
 */
public class ImageListActivity extends FragmentActivity implements
		ImageListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_list);

		if (findViewById(R.id.image_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ImageListFragment) getSupportFragmentManager().findFragmentById(
					R.id.image_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link ImageListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ImageDetailFragment.ARG_ITEM_ID, id);
			ImageDetailFragment fragment = new ImageDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.image_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ImageDetailActivity.class);
			detailIntent.putExtra(ImageDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
