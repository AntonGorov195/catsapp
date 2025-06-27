package co.il.catsapp.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.tv.TvContract
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.il.catsapp.R
import co.il.catsapp.databinding.FragmentCatBinding
import co.il.catsapp.databinding.FragmentFavoriteListBinding
import co.il.catsapp.databinding.RemoveCatDialogBinding
import co.il.catsapp.services.AlarmReceiver
import co.il.catsapp.services.AlarmSoundService
import co.il.catsapp.ui.view_models.CatViewModel
import co.il.catsapp.ui.view_models.FavoriteListViewModel
import co.il.catsapp.utils.CatAlarmHelper
import co.il.catsapp.utils.Resource
import co.il.catsapp.utils.autoCleared
import co.il.catsapp.utils.setGlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.sqrt
import kotlin.random.Random

@AndroidEntryPoint
class CatFragment : Fragment() {

    private val viewModel: CatViewModel by viewModels()
    private var removeCatDialog: AlertDialog? = null

    private var binding: FragmentCatBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val id = CatFragmentArgs.fromBundle(it).catId
            viewModel.cat.observe(viewLifecycleOwner) { catResource ->
                when (val cat = catResource.status) {
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Couldn't find your cat :(", Toast.LENGTH_SHORT).show()
                        Log.e("cat not found", "Couldn't find cat with id $id")
                        findNavController().popBackStack()
                    }
                    is Resource.Loading -> {
                        // No need for this.
                        // The default behavior is to show a loading screen.
                    }
                    is Resource.Success -> {
                        binding.catName.text = cat.data!!.name
                        setGlideImage(binding.root, binding.catImage, cat.data.image)

                        binding.reminderBtn.setOnClickListener {
                            val delay = 5 * 1000L // Example: 5 seconds
                            CatAlarmHelper.cancelAlarm(requireContext(), id)
                            CatAlarmHelper.setAlarm(requireContext(), cat.data, delay)
                            Toast.makeText(requireContext(), "Alarm set!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        binding.removeCat.setOnClickListener {
                            viewModel.startRemoving()
                        }
                    }
                }
            }
            viewModel.setId(id)
            viewModel.isRemoving.observe(viewLifecycleOwner) {
                if (it) {
                    when (val catResource = (viewModel.cat.value ?: return@observe).status) {
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "Failed opening cat deletion dialog.", Toast.LENGTH_SHORT).show()
                            return@observe
                        }
                        is Resource.Loading -> return@observe
                        is Resource.Success -> {
                            val cat = catResource.data!!
                            val removeCatDialogBinding =
                                RemoveCatDialogBinding.inflate(layoutInflater)
                            removeCatDialogBinding.noRemove.setOnClickListener {
                                viewModel.endRemoving()
                            }
                            removeCatDialogBinding.yesRemove.setOnClickListener {
                                viewModel.removeCat(cat)
                                viewModel.endRemoving()
                                findNavController().popBackStack()
                            }
                            setGlideImage(
                                removeCatDialogBinding.root,
                                removeCatDialogBinding.catImage,
                                cat.image
                            )
                            removeCatDialogBinding.removeCatText.text =
                                getString(R.string.are_you_sure_you_want_to_remove, cat.name)
                            removeCatDialog = AlertDialog.Builder(requireContext())
                                .setView(removeCatDialogBinding.root).create()
                            removeCatDialog?.show()
                        }
                    }
                } else {
                    removeCatDialog?.cancel()
                }
            }
        } ?: {
            Toast.makeText(requireContext(), "Failed finding your cat's id", Toast.LENGTH_SHORT)
                .show()
            findNavController().popBackStack()
        }
        binding.catImage.setOnClickListener {
            val player = MediaPlayer.create(requireContext(), R.raw.meow_sfx)
            player.setOnCompletionListener {
                it.stop()
                it.release()
            }
            val p = player.playbackParams
            p.pitch = Random.nextFloat() + 0.5f
            p.speed = sqrt(Random.nextFloat() + 0.5f)
            player.playbackParams = p
            player.start()
            requireContext().stopService(Intent(requireContext(), AlarmSoundService::class.java))
        }
    }
}