/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.soltelec.consolaentrada.custom;

import javax.swing.*;
import java.util.List;

/**
 *
 * @author Usuario
 */
public final class MyOwnComboBoxModel extends AbstractListModel  implements MutableComboBoxModel {

    private List<? extends Object> backList;
    Object selectedObject;

    public MyOwnComboBoxModel(List<? extends Object> backList) {
        this.backList = backList;
        if(getSize() > 0)
            setSelectedItem(backList.get(0));

    }



    @Override
    public void setSelectedItem(Object anItem) {
         if ((selectedObject != null && !selectedObject.equals( anItem )) ||
         selectedObject == null && anItem != null) {
         selectedObject = anItem;
            fireContentsChanged(this, -1, -1);
         }
    }

    @Override
    public Object getSelectedItem() {
        return selectedObject;
    }

    @Override
    public int getSize() {
       return backList.size();
    }

    @Override
    public Object getElementAt(int index) {
        if ( index >= 0 && index < backList.size() )
             return backList.get(index);
         else
             return null;
    }




    @Override
    public void addElement(Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public void removeElement(Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public void insertElementAt(Object obj, int index) {

    }


    @Override
    public void removeElementAt(int index) {
        backList.remove(index);
    }

    public void removeAllElements() {
        if ( backList.size() > 0 ) {
             int firstIndex = 0;
             int lastIndex = backList.size() - 1;
             backList.clear();
             selectedObject = null;
             fireIntervalRemoved(this, firstIndex, lastIndex);
         } else {
         selectedObject = null;
    }
   }

    public void setBackList(List<? extends Object> bList) {
        this.backList = bList;
        fireContentsChanged(this,-1,-1);
        if(getSize() > 0)
        selectedObject = backList.get(0);
    }




}
