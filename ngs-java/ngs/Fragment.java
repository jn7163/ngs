/*===========================================================================
*
*                            PUBLIC DOMAIN NOTICE
*               National Center for Biotechnology Information
*
*  This software/database is a "United States Government Work" under the
*  terms of the United States Copyright Act.  It was written as part of
*  the author's official duties as a United States Government employee and
*  thus cannot be copyrighted.  This software/database is freely available
*  to the public for use. The National Library of Medicine and the U.S.
*  Government have not placed any restriction on its use or reproduction.
*
*  Although all reasonable efforts have been taken to ensure the accuracy
*  and reliability of the software and data, the NLM and the U.S.
*  Government do not and cannot warrant the performance or results that
*  may be obtained by using this software or data. The NLM and the U.S.
*  Government disclaim all warranties, express or implied, including
*  warranties of performance, merchantability or fitness for any particular
*  purpose.
*
*  Please cite the author in any work or product based on this material.
*
* ===========================================================================
*
*/

package ngs;


/**
 * Represents an NGS biological fragment
 */
public interface Fragment
{

    /**
     * getFragmentId
     */
    String getFragmentId ()
        throws ErrorMsg;

    /*------------------------------------------------------------------
     * fragment details
     */
   
    /** 
     * getFragmentBases
     * @return sequence bases
     */
    String getFragmentBases ()
        throws ErrorMsg;
    
    /** 
     * getFragmentBases
     * @param offset is zero-based and non-negative
     * @return sequence bases
     */
    String getFragmentBases ( long offset )
        throws ErrorMsg, IndexOutOfBoundsException;
    
    /** 
     * getFragmentBases
     * @param offset is zero-based and non-negative
     * @param length must be &ge; 0
     * @return sequence bases
     */
    String getFragmentBases ( long offset, long length )
        throws ErrorMsg, IndexOutOfBoundsException;


    /** 
     * getFragmentQualities using ASCII offset of 33
     * @return phred quality values for the whole fragment
     */
    String getFragmentQualities ()
        throws ErrorMsg;
    
    /** 
     * getFragmentQualities using ASCII offset of 33
     * @param offset is zero-based and non-negative
     * @return phred quality values
     */
    String getFragmentQualities ( long offset )
        throws ErrorMsg, IndexOutOfBoundsException;
    
    /** 
     * getFragmentQualities using ASCII offset of 33
     * @param offset is zero-based and non-negative
     * @param length must be &ge; 0
     * @return phred quality values
     */
    String getFragmentQualities ( long offset, long length )
        throws ErrorMsg, IndexOutOfBoundsException;
}
