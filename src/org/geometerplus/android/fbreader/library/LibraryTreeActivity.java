/*
 * Copyright (C) 2010-2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.library;

import android.content.Intent;
import android.os.Bundle;

import org.geometerplus.fbreader.tree.FBTree;

public class LibraryTreeActivity extends LibraryBaseActivity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (DatabaseInstance == null || LibraryInstance == null) {
			finish();
			return;
		}

		final Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			if (runSearch(intent)) {
				startActivity(intent
					.setAction(ACTION_FOUND)
					.setClass(getApplicationContext(), LibraryTopLevelActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
				);
			} else {
				showNotFoundToast();
				finish();
			}
			return;
		}

		String title = null;
		if (myTreePath.length == 1) {
			title = myResource.getResource(myTreePath[0]).getResource("summary").getValue();
			final String parameter = intent.getStringExtra(PARAMETER_KEY);
			if (parameter != null) {
				title = title.replace("%s", parameter);
			}
		} else {
			title = myTreePath[myTreePath.length - 1];
		}
		setTitle(title);

		FBTree tree = null;
		if (PATH_RECENT.equals(myTreePath[0])) {
			tree = LibraryInstance.recentBooks();
		} else if (PATH_SEARCH_RESULTS.equals(myTreePath[0])) {
			tree = LibraryInstance.searchResults();
		} else if (PATH_BY_AUTHOR.equals(myTreePath[0])) {
			tree = LibraryInstance.byAuthor();
		} else if (PATH_BY_TITLE.equals(myTreePath[0])) {
			tree = LibraryInstance.byTitle();
		} else if (PATH_BY_TAG.equals(myTreePath[0])) {
			tree = LibraryInstance.byTag();
		} else if (PATH_FAVORITES.equals(myTreePath[0])) {
			tree = LibraryInstance.favorites();
		}
        
		for (int i = 1; i < myTreePath.length; ++i) {
			if (tree == null) {
				break;
			}
			tree = tree.getSubTreeByName(myTreePath[i]);
		}

		if (tree != null) {
			final ListAdapter adapter = new ListAdapter(this, tree.subTrees());
			setSelection(adapter.getFirstSelectedItemIndex());
		}
	}
}
