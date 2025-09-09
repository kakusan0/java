$(function () {
  // 1. PC用サイドバーの開閉
  $('#pcSidebarToggle').on('click', function () {
    $('#sidebarMenu, .main-content').toggleClass('is-collapsed');
  });

  // 2. スマホでのスワイプ開閉機能
  const $sidebarElement = $('#sidebarMenu');
  if ($sidebarElement.length) {
    const sidebar = bootstrap.Offcanvas.getOrCreateInstance($sidebarElement[0]);
    // ... existing code ...
  }

  // 3. 画面の実際の高さを取得してCSS変数にセットする機能
  const setAppHeight = () => {
    $('html').css('--app-height', `${window.innerHeight}px`);
  };
  $(window).on('resize', setAppHeight);
  setAppHeight();

  // 4. トースト表示機能
  $('#liveToastBtn').on('click', function () {
    const toastLiveExample = $('#liveToast');
    const toast = bootstrap.Toast.getOrCreateInstance(toastLiveExample[0]);
    toast.show();
  });

  // 5. モーダルごとの背景色を設定
  const setErrorBackdrop = function () {
    $('.modal-backdrop').last().addClass('backdrop-error');
  };
  const setSelectBackdrop = function () {
    $('.modal-backdrop').last().addClass('backdrop-select');
  };
  $('#errorModal').on('shown.bs.modal', setErrorBackdrop);
  $('#scrollableModal').on('shown.bs.modal', setSelectBackdrop);

  $(window).on("load", function () {
    $("#createPasskey").on("click", () => createPasskey());
    $("#registerPasskey").on("click", () => createPasskey()); // 新しいボタンID
    $("#authenticatePasskey").on("click", () => signInWithPasskey());
  });

  // 共通: JSON レスポンス安全取得
  async function safeJson(resp) {
    const ct = resp.headers.get("content-type") || "";
    if (!resp.ok) {
      const text = await resp.text().catch(() => "");
      throw new Error(`HTTP ${resp.status}${text ? `: ${text.substring(0, 200)}` : ""}`);
    }
    if (!ct.toLowerCase().includes("application/json")) {
      const text = await resp.text().catch(() => "");
      throw new Error(`Non-JSON response: ${ct}${text ? ` | body: ${text.substring(0, 200)}` : ""}`);
    }
    return resp.json();
  }

  /*
   * Register
   */
  async function createPasskey() {
    const csrfToken = getCsrfToken();

    // Helper function to update status display
    function updateStatus(message) {
      $("#statusCreatePasskey").text(message).show();
    }

    updateStatus("パスキー登録の準備中...");

    // get option
    let optionsJSON;
    try {
      let optResp = await fetch("/webauthn/register/options", {
        method: "POST",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
          "X-CSRF-TOKEN": csrfToken
        }
      });
      optionsJSON = await safeJson(optResp);
    } catch (e) {
      updateStatus("Error: " + e.message);
      return;
    }

    updateStatus("認証機器での操作を完了してください...");

    // ... existing code ...
    let attResp;
    try {
      let options = { optionsJSON, useAutoRegister: false };
      attResp = await SimpleWebAuthnBrowser.startRegistration(options);
    } catch (e) {
      if (e.name === "InvalidStateError") {
        updateStatus("Error: Authenticator was probably already registered by user");
      } else {
        updateStatus("Error: " + e);
      }
      return;
    }

    updateStatus("パスキー登録を完了しています...");

    // verify
    try {
      let publicKeyCredential = {
        publicKey: {
          credential: attResp,
          label: "hoge",
        }
      };

      const verificationResp = await fetch("/webauthn/register", {
        method: "POST",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
          "X-CSRF-TOKEN": csrfToken
        },
        body: JSON.stringify(publicKeyCredential),
      });

      const verificationJSON = await safeJson(verificationResp);
      if (verificationJSON && verificationJSON.success) {
        updateStatus("パスキー登録が成功しました！");
      } else {
        updateStatus("パスキー登録に失敗しました。");
      }
    } catch (e) {
      updateStatus("Error: " + e.message);
    }
  }

  /*
   * Authenticate
   */
  async function signInWithPasskey() {
    const csrfToken = getCsrfToken();

    // get option
    let optionsJSON;
    try {
      const resp = await fetch("/webauthn/authenticate/options", {
        method: "POST",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
          "X-CSRF-TOKEN": csrfToken
        }
      });
      optionsJSON = await safeJson(resp);
    } catch (e) {
      $("#statusSigninWithPasskey").text("Error: " + e.message);
      return;
    }

    // startAuthentication
    let asseResp;
    try {
      asseResp = await SimpleWebAuthnBrowser.startAuthentication({ optionsJSON });
    } catch (e) {
      $("#statusSigninWithPasskey").text("Error: " + e);
      return;
    }

    // verify
    try {
      const verificationResp = await fetch("/login/webauthn", {
        method: "POST",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
          "X-CSRF-TOKEN": csrfToken,
        },
        body: JSON.stringify(asseResp),
      });

      const verificationJSON = await safeJson(verificationResp);
      if (verificationJSON.authenticated) {
        if (verificationJSON.redirectUrl) {
          window.location.href = verificationJSON.redirectUrl;
        }
      } else {
        $("#statusSigninWithPasskey").text("Error: " + (verificationJSON.message || "Authentication failed"));
      }
    } catch (e) {
      $("#statusSigninWithPasskey").text("Error: " + e.message);
    }
  }

  /*
   * Functions
   */
  function getCsrfToken() {
    const csrfInput = document.querySelector('input[name="_csrf"]');
    return csrfInput ? csrfInput.value : null;
  }
});
