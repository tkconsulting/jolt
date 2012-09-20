package com.bazaarvoice.jolt.defaultr;

import com.bazaarvoice.jolt.Defaultr;

import java.util.Comparator;

public enum OPS {

    STAR, OR, LITERAL;

    public static OPS parse( String key ) {
        if ( key.contains( Defaultr.WildCards.STAR ) ){

            if ( ! Defaultr.WildCards.STAR.equals( key ) ) {
                throw new IllegalArgumentException("Defaultr key " + key + " is invalid.  * keys can only contain *, and no other characters." );
            }

            return STAR;
        }
        if ( key.contains( Defaultr.WildCards.OR ) ) {
            return OR;
        }
        return LITERAL;
    }

    public static class OpsPrecedenceComparator implements Comparator<OPS> {
        /**
         * The order we want to apply Defaultr logic is Literals, Or, and then Star.
         * Since we walk the sorted data from 0 to n, that means Literals need to low, and Star should be high.
         */
        @Override
        public int compare(OPS ops, OPS ops1) {

            // a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
            // s < s1 -> -1
            // s = s1 -> 0
            // s > s1 -> 1

            if ( ops == ops1 ) {
                return 0;
            }

            if ( STAR == ops ) {
                return 1;
            }
            if ( LITERAL == ops ) {
                return -1;
            }

            // if we get here, "ops" has to equal OR
            if ( STAR == ops1) {
                return -1;
            }
            if ( LITERAL == ops1 ) {
                return 1;
            }

            // both are ORs, should never get here
            throw new IllegalArgumentException( "Someone has added an op type without changing this method." );
        }
    }
}