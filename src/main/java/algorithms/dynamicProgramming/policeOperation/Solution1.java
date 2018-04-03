/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.dynamicProgramming.policeOperation;

/**
 *
 * @author andrea
 */
public class Solution1 {
    
    https://github.com/marioyc/Online-Judge-Solutions/blob/master/hackerrank/101aug14/police-operation.cpp
    
    
    /*
  j < k: j better than k iff
  s[j]-s[k] + a[j+1]^2-a[k+1]^2 + 2a[i](a[k+1]-a[j+1]) < 0
  2a[i](a[k+1]-a[j+1])  < a[k+1]^2-a[j+1]^2+s[k]-s[j]
*/
#include <algorithm>
#include <cstdio>
using namespace std;

#define FOR(i, a, b) for (int i = (a); i < (b); i++)
#define REP(i, n) for (int i = 0; i < (n); i++)
#define REP1(i, n) for (int i = 1; i <= (n); i++)
#define ROF(i, a, b) for (int i = (b); --i >= (a); )
#define P(x) ((x)*(x))
#define F(x,y) (P(a[(y)+1])-P(a[(x)+1])+dp[y]-dp[x])

int ri()
{
  int x;
  scanf("%d", &x);
  return x;
}

const int N = 2000000;
int q[N+1];
long a[N+1], dp[N+1];

int main()
{
  int n = ri(), x = ri(), *fr = q, *re = q+1;
  REP1(i, n)
    a[i] = ri();
  // opzionale: n = unique(a+1, a+n+1) - (a+1);
  REP1(i, n) {
    while (fr+1 < re && 2*a[i]*(a[fr[1]+1]-a[*fr+1]) > F(*fr, fr[1]))
      fr++;
    dp[i] = dp[*fr]+P(a[i]-a[*fr+1])+x;
    if (i < n) {
      while (fr <= re-2 && F(re[-2], re[-1]) / (a[re[-1]+1]-a[re[-2]+1]) > F(re[-1], i) / (a[i+1]-a[re[-1]+1]))
        re--;
      *re++ = i;
    }
  }
  printf("%ld\n", dp[n]);
}

}
