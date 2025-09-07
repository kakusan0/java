$(function () {
  // 1. PC用サイドバーの開閉
  $('#pcSidebarToggle').on('click', function () {
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
    $(document).on('touchstart', function (e) {
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
    }).on('touchend', function (e) {
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
    }).on('touchcancel', function () {
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
  $('#liveToastBtn').on('click', function () {
    const toastLiveExample = $('#liveToast');
    const toast = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    toast.show();
  });

  // 5. モーダルごとの背景色を設定
  const setErrorBackdrop = function () {
    // 複数のモーダルが重なる場合も考慮し、一番手前(最後)の背景要素を取得
    $('.modal-backdrop').last().addClass('backdrop-error');
  };

  const setSelectBackdrop = function () {
    $('.modal-backdrop').last().addClass('backdrop-select');
  };

  // イベントリスナーを紐付け
  $('#errorModal').on('shown.bs.modal', setErrorBackdrop);
  $('#scrollableModal').on('shown.bs.modal', setSelectBackdrop);

  const passkeyBtn = document.getElementById('passkeySignInBtn');
  if (passkeyBtn && !window.PublicKeyCredential) {
    passkeyBtn.setAttribute('disabled', 'disabled');
    passkeyBtn.setAttribute('title', 'このブラウザはパスキーに対応していません');
  }

  // CSRF（meta タグから取得）
  const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';
  const CSRF_TOKEN  = document.querySelector('meta[name="_csrf"]')?.content || '';

  // Base64URL <-> ArrayBuffer ヘルパ
  function b64urlToBuf(b64url) {
    const pad = '='.repeat((4 - b64url.length % 4) % 4);
    const b64 = (b64url.replace(/-/g, '+').replace(/_/g, '/')) + pad;
    const str = atob(b64);
    const buf = new ArrayBuffer(str.length);
    const bytes = new Uint8Array(buf);
    for (let i = 0; i < str.length; i++) bytes[i] = str.charCodeAt(i);
    return buf;
  }
  function bufToB64url(buf) {
    const bytes = new Uint8Array(buf);
    let str = '';
    for (let i = 0; i < bytes.byteLength; i++) str += String.fromCharCode(bytes[i]);
    return btoa(str).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
  }

  async function passkeySignIn() {
    try {
      // 1) 認証オプション取得
      const optRes = await fetch('/webauthn/authentication/options', {
        method: 'POST',
        headers: { [CSRF_HEADER]: CSRF_TOKEN }
      });
      if (!optRes.ok) throw new Error('オプション取得に失敗しました');
      const options = await optRes.json();

      // 2) challenge / allowCredentials を ArrayBuffer に変換
      options.publicKey.challenge = b64urlToBuf(options.publicKey.challenge);
      if (Array.isArray(options.publicKey.allowCredentials)) {
        options.publicKey.allowCredentials = options.publicKey.allowCredentials.map(c => ({
          ...c, id: b64urlToBuf(c.id)
        }));
      }

      // 3) デバイス側で認証
      const cred = await navigator.credentials.get({ publicKey: options.publicKey });

      // 4) サーバ送信用に整形
      const authData = {
        id: cred.id,
        type: cred.type,
        rawId: bufToB64url(cred.rawId),
        response: {
          authenticatorData: bufToB64url(cred.response.authenticatorData),
          clientDataJSON:    bufToB64url(cred.response.clientDataJSON),
          signature:         bufToB64url(cred.response.signature),
          userHandle:        cred.response.userHandle ? bufToB64url(cred.response.userHandle) : null
        },
        clientExtensionResults: cred.getClientExtensionResults?.() || {}
      };

      // 5) 検証
      const verifyRes = await fetch('/webauthn/authentication/verify', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          [CSRF_HEADER]: CSRF_TOKEN
        },
        body: JSON.stringify(authData)
      });
      if (!verifyRes.ok) throw new Error('検証に失敗しました');

      // 6) 成功後にトップ等へ遷移
      window.location.href = '/';
    } catch (e) {
      console.error(e);
      alert('パスキー認証に失敗しました。別の方法でお試しください。');
    }
  }

  if (passkeyBtn) {
    passkeyBtn.addEventListener('click', passkeySignIn);
  }
});
