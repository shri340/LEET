
import java.util.*;

class Solution {
    private int[][] up;
    private int[] depth, tin, tout, prefix;
    private char[] chars;
    private int log;
    private Fenwick bit;

    public List<Boolean> palindromePath(int n, int[][] edges, String s,
                                        String[] queries) {
        List<Integer>[] graph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            graph[e[0]].add(e[1]);
            graph[e[1]].add(e[0]);
        }

        log = 1;
        while ((1 << log) <= n) {
            log++;
        }

        up = new int[log][n];
        depth = new int[n];
        tin = new int[n];
        tout = new int[n];
        prefix = new int[n];
        chars = s.toCharArray();

        int[] parent = new int[n];
        int[] order = new int[n];
        int[] stack = new int[n];
        Arrays.fill(parent, -1);

        int top = 0, count = 0;
        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[--top];
            tin[u] = count;
            order[count++] = u;
            up[0][u] = parent[u];

            for (int v : graph[u]) {
                if (v == parent[u]) {
                    continue;
                }

                parent[v] = u;
                depth[v] = depth[u] + 1;
                prefix[v] = prefix[u] ^ (1 << (chars[v] - 'a'));
                stack[top++] = v;
            }
        }

        for (int k = 1; k < log; k++) {
            for (int u = 0; u < n; u++) {
                up[k][u] = up[k - 1][up[k - 1][u]];
            }
        }

        int[] size = new int[n];
        Arrays.fill(size, 1);

        for (int i = n - 1; i > 0; i--) {
            int u = order[i];
            size[parent[u]] += size[u];
        }

        for (int u = 0; u < n; u++) {
            tout[u] = tin[u] + size[u] - 1;
        }

        bit = new Fenwick(n);
        List<Boolean> result = new ArrayList<>();

        for (String q : queries) {
            String[] p = q.split(" ");
            int u = Integer.parseInt(p[1]);

            if (p[0].equals("update")) {
                char c = p[2].charAt(0);

                int delta = (1 << (chars[u] - 'a'))
                          ^ (1 << (c - 'a'));

                if (delta != 0) {
                    bit.rangeXor(tin[u], tout[u], delta);
                    chars[u] = c;
                }
            } else {
                int v = Integer.parseInt(p[2]);
                int lca = getLCA(u, v);

                int mask = getPrefix(u) ^ getPrefix(v)
                         ^ (1 << (chars[lca] - 'a'));

                result.add((mask & (mask - 1)) == 0);
            }
        }

        return result;
    }

    private int getPrefix(int u) {
        return prefix[u] ^ bit.query(tin[u]);
    }

    private int getLCA(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        int diff = depth[u] - depth[v];

        for (int k = 0; k < log; k++) {
            if (((diff >> k) & 1) != 0) {
                u = up[k][u];
            }
        }

        if (u == v) {
            return u;
        }

        for (int k = log - 1; k >= 0; k--) {
            if (up[k][u] != up[k][v]) {
                u = up[k][u];
                v = up[k][v];
            }
        }

        return up[0][u];
    }

    private static class Fenwick {
        private final int[] tree;
        private final int n;

        Fenwick(int n) {
            this.n = n;
            tree = new int[n + 2];
        }

        void add(int index, int value) {
            for (int i = index + 1; i <= n; i += i & -i) {
                tree[i] ^= value;
            }
        }

        void rangeXor(int left, int right, int value) {
            add(left, value);

            if (right + 1 < n) {
                add(right + 1, value);
            }
        }

        int query(int index) {
            int result = 0;

            for (int i = index + 1; i > 0; i -= i & -i) {
                result ^= tree[i];
            }

            return result;
        }
    }
}
