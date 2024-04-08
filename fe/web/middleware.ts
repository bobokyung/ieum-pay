import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import { getCookie } from './utils/cookie';

export function middleware(request: NextRequest) {
  // 리다이렉트 조건
  if (!request.cookies.get('access_token')) {
    //   if (!getCookie('access_token')) {
    return NextResponse.redirect(new URL('/landing', request.url));
  }
}

export const config = {
  // 이 Middleware가 동작할 경로들을 추가해주면된다.
  matcher: [
    '/main',
    '/fundraising/:path*',
    '/history/:path*',
    '/settings/:path*',
    '/my-page/:path*',
    '/payment/:path*',
  ],
};
