package gDrive.utils;

import java.beans.PropertyDescriptor;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;

@SuppressWarnings("rawtypes")
public class ObjectComparator implements Comparator {

	private String sortableProperty;
	private String order;
	private boolean caseSensitiveFlg;

	/*
	 * @param Object
	 * 
	 * @param Object
	 *
	 * @return int
	 */
	public int compare(Object o1, Object o2) {
		try {
			StringTokenizer st = new StringTokenizer(sortableProperty, ".");
			while (st.hasMoreElements()) {
				String property = st.nextToken();
				property = property.substring(0, 1).toUpperCase() + property.substring(1);

				o1 = new PropertyDescriptor(property, o1.getClass()).getReadMethod().invoke(o1);
				o2 = new PropertyDescriptor(property, o2.getClass()).getReadMethod().invoke(o2);
			}
			if (o1 == o2 || (o1 == null && o2 == null)) {
				return 0;
			}
			if ((order != null) && order.equals("asc")) {
				if (o1 == null) {
					return -1;
				} else if (o2 == null) {
					return 1;
				}
			} else {
				if (o1 == null) {
					return 1;
				} else if (o2 == null) {
					return -1;
				}
			}
			if (o1 instanceof String) {
				if (caseSensitiveFlg) {
					if ((order != null) && order.equals("asc"))
						return ((String) o1).compareTo(((String) o2));
					else
						return ((String) o2).compareTo(((String) o1));
				} else {
					if ((order != null) && order.equals("asc"))
						return ((String) o1).compareToIgnoreCase(((String) o2));
					else
						return ((String) o2).compareToIgnoreCase(((String) o1));
				}
			} else if (o1 instanceof Short) {
				if ((order != null) && order.equals("asc"))
					return Short.compare(((Short) o1).shortValue(), ((Short) o2).shortValue());
				else
					return Short.compare(((Short) o2).shortValue(), ((Short) o1).shortValue());
			} else if (o1 instanceof Integer) {
				if ((order != null) && order.equals("asc"))
					return Integer.compare(((Integer) o1).intValue(), ((Integer) o2).intValue());
				else
					return Integer.compare(((Integer) o2).intValue(), ((Integer) o1).intValue());
			} else if (o1 instanceof Long) {
				if ((order != null) && order.equals("asc"))
					return Long.compare(((Long) o1).intValue(), ((Long) o2).intValue());
				else
					return Long.compare(((Long) o2).intValue(), ((Long) o1).intValue());
			} else if (o1 instanceof Double) {
				if ((order != null) && order.equals("asc"))
					return Double.compare((Double) o1, (Double) o2);
				else
					return Double.compare((Double) o2, (Double) o1);
			} else if (o1 instanceof Float) {
				if ((order != null) && order.equals("asc"))
					return Float.compare((Float) o1, (Float) o2);
				else
					return Float.compare((Float) o2, (Float) o1);
			} else if (o1 instanceof Date) {
				if ((order != null) && order.equals("asc"))
					return ((Date) o1).compareTo((Date) o2);
				else
					return ((Date) o2).compareTo((Date) o1);
			} else {
				System.out.println("***Instance of property not available***");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public void setSortingData(String sortableProperty, String order, boolean caseSensitiveFlg) {
		this.sortableProperty = sortableProperty;
		this.order = order;
		this.caseSensitiveFlg = caseSensitiveFlg;
	}

}
