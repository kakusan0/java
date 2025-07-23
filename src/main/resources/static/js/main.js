$(function() {
  // jQueryの$(document).ready()の短縮形。DOMの読み込み完了後に実行します。

  // 1. PC用サイドバーの開閉
  $('#pcSidebarToggle').on('click', function() {
    // 複数の要素に同じクラスを一度に付け外しできます
    $('#sidebarMenu, .main-content').toggleClass('is-collapsed');
  });

  // 2. スマホでのスワイプ開閉機能
  const $sidebarElement = $('#sidebarMenu');
  if ($sidebarElement.length) { // 要素が存在するかチェック
    // BootstrapのOffcanvasインスタンスを取得
    const sidebar = bootstrap.Offcanvas.getOrCreateInstance($sidebarElement[0]);

    // スワイプ検知の閾値
    const edgeThreshold = 50;     // スワイプを開始して良い画面左端からの範囲(px)
    const swipeThreshold = 80;    // スワイプとして認識する最小水平距離(px)
    const verticalThreshold = 75; // スワイプとして許容する最大の垂直移動距離(px)

    let touchStartX = 0;
    let touchStartY = 0;
    let isSwipingFromEdge = false;

    // イベントのチェーン（連結）で記述を簡潔に
    $(document).on('touchstart', function(e) {
      const isMobileLandscape = window.matchMedia("(max-height: 500px) and (orientation: landscape)").matches;
      if (isMobileLandscape) {
        return; // 横画面ではスワイプ処理を開始しない
      }
      // jQueryのイベントオブジェクトから元のtouchイベントを取得
      const touch = e.originalEvent.touches[0];
      if (e.originalEvent.touches.length === 1 && !$sidebarElement.hasClass('show') && touch.clientX < edgeThreshold) {
        touchStartX = touch.clientX;
        touchStartY = touch.clientY;
        isSwipingFromEdge = true;
      }
    }).on('touchend', function(e) {
      if (!isSwipingFromEdge) {
        return;
      }
      isSwipingFromEdge = false;

      const touch = e.originalEvent.changedTouches[0];
      const deltaX = touch.clientX - touchStartX;
      const deltaY = Math.abs(touch.clientY - touchStartY);

      if (deltaX > swipeThreshold && deltaY < verticalThreshold) {
        sidebar.show();
      }
    }).on('touchcancel', function() {
      isSwipingFromEdge = false;
    });
  }

  // 3. 画面の実際の高さを取得してCSS変数にセットする機能
  const setAppHeight = () => {
    // $('html')でdocumentElementを選択し、.css()でカスタムプロパティを設定
    $('html').css('--app-height', `${window.innerHeight}px`);
  };

  // ウィンドウのリサイズイベントにsetAppHeightを紐付け
  $(window).on('resize', setAppHeight);

  // 初期読み込み時にも実行
  setAppHeight();

  // 4. トースト表示機能
  $('#liveToastBtn').on('click', function() {
    const toastLiveExample = $('#liveToast');
    const toast = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    toast.show();
  });
});
