package es.ucm.gdv.offtheline;

public class Utils {

    public static class Point {
        double x = 0.0;
        double y = 0.0;

        public Point() {
            x = 0.0;
            y = 0.0;
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void set(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    public static class Segment {
        Point from;
        Point to;

        public Segment(double fromX, double fromY, double toX, double toY) {
            from = new Point(fromX, fromY);
            to = new Point(toX, toY);
        }

        public Segment(Point from, Point to) {
            this.from = from;
            this.to = to;
        }

        public double sqDistancePointToSegment(Point point) {
            double x = point.getX();
            double y = point.getY();
            double x1 = from.getX();
            double y1 = from.getY();
            double x2 = to.getX();
            double y2 = to.getY();

            double A = x - x1;
            double B = y - y1;
            double C = x2 - x1;
            double D = y2 - y1;

            double dot = A * C + B * D;
            double len_sq = C * C + D * D;
            double param = -1;
            if (len_sq != 0) //in case of 0 length line
                param = dot / len_sq;

            double xx, yy;

            if (param < 0) {
                xx = x1;
                yy = y1;
            }
            else if (param > 1) {
                xx = x2;
                yy = y2;
            }
            else {
                xx = x1 + param * C;
                yy = y1 + param * D;
            }

            double dx = x - xx;
            double dy = y - yy;

            return dx * dx + dy * dy;
        }

        // To find orientation of ordered triplet (a, b, c).
        // The function returns following values
        // 0 --> a, b and c are colinear
        // 1 --> Clockwise
        // 2 --> Counterclockwise
        public static int orientation(Point a, Point b, Point c)
        {
            double val = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);

            if (val == 0.0) return 0; // colinear
            return (val > 0.0) ? 1 : 2; // clock or counterclock wise
        }

        // Given three colinear points a, b, c, the function checks if
        // point b lies on line segment 'ac'
        static boolean onSegment(Point a, Point b, Point c)
        {
            if (b.x <= Math.max(a.x, c.x) && b.x >= Math.min(a.x, c.x) &&
                    b.y <= Math.max(a.y, c.y) && b.y >= Math.min(a.y, c.y))
                return true;

            return false;
        }

        public static boolean doIntersect(Point a1, Point a2, Point b1, Point b2)
        {
            // Find the four orientations needed for general and
            // special cases
            int o1 = orientation(a1, a2, b1);
            int o2 = orientation(a1, a2, b2);
            int o3 = orientation(b1, b2, a1);
            int o4 = orientation(b1, b2, a2);

            // General case
            if (o1 != o2 && o3 != o4)
                return true;

            // Special Cases
            // p1, q1 and p2 are colinear and p2 lies on segment p1q1
            /*if (o1 == 0 && onSegment(a1, b1, a2)) return true;

            // p1, q1 and q2 are colinear and q2 lies on segment p1q1
            if (o2 == 0 && onSegment(a1, b2, a2)) return true;

            // p2, q2 and p1 are colinear and p1 lies on segment p2q2
            if (o3 == 0 && onSegment(b1, a1, b2)) return true;

            // p2, q2 and q1 are colinear and q1 lies on segment p2q2
            if (o4 == 0 && onSegment(b1, a2, b2)) return true;*/
            return false;
        }

        public double getX1() {
            return from.getX();
        }

        public double getY1() {
            return from.getY();
        }

        public double getX2() {
            return to.getX();
        }

        public double getY2() {
            return to.getY();
        }
    }

    // Devuelve el punto intersección entre dos segmentos
    public static Point segmentsIntersection(Segment a, Segment b) {
        if (Segment.doIntersect(a.from, a.to, b.from, b.to)) {
            double d = (a.getX1() - a.getX2()) * (b.getY2() - b.getY1()) - (a.getY1() - a.getY2()) * (b.getX2() - b.getX1());
            double da = (a.getX1() - b.getX1()) * (b.getY2() - b.getY1()) - (a.getY1() - b.getY1()) * (b.getX2() - b.getX1());
            double ta = da / d;
            Point p = new Point();
            p.set(a.getX1() + ta * (a.getX2() - a.getX1()), a.getY1() + ta * (a.getY2() - a.getY1()));
            return p;
        } else
            return null;
    }

    // Devuelve el cuadrado de la distancia entre un segmento y un punto
    public static double sqrDistancePointSegment(Segment segment, Point point) {
        return segment.sqDistancePointToSegment(point);
    }

}
