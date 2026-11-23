# نشر Pop-up على GitHub Pages

اسم حساب GitHub حسب سجل البناء: `ailrhame`
اسم المستودع حسب سجل البناء: `klencod-kotlin-Pop-up_N5CR4_Android_GitHubPages`

1. افتح المستودع:
   https://github.com/ailrhame/klencod-kotlin-Pop-up_N5CR4_Android_GitHubPages
2. تأكد أن `docs/index.html` موجود في جذر المستودع داخل مجلد docs.
3. افتح Settings → Pages.
4. تحت Build and deployment اختر Deploy from a branch.
5. اختر Branch: `main` و Folder: `/docs` ثم Save.
6. انتظر نجاح مهمة `pages build and deployment` في Actions.

رابط الموقع بعد النشر:
https://ailrhame.github.io/klencod-kotlin-Pop-up_N5CR4_Android_GitHubPages/

تم تحديث `pagesBaseUrl` في تطبيق Android ليستخدم الرابط أعلاه.
بعد نشر الموقع، أعد بناء APK من GitHub Actions وثبّت النسخة الجديدة.

ملاحظة: إذا كان اسم الحساب أو المستودع مختلفاً فعلياً، يجب تعديل رابط Pages في `MainActivity.kt` وفق الاسم الظاهر في شريط عنوان المستودع.
